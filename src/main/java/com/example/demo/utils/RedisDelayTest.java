package com.example.demo.utils;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * redis实现延时队列
 *
 * @author zhou.xy
 * @since 2019/7/18
 */
public class RedisDelayTest {

    private static final String ADDRESS = "127.0.0.1";
    private static final Integer PORT = 6379;
    private static final String KEY = "test_delay_order_id";
    private static JedisPool jedisPool = new JedisPool(ADDRESS, PORT);
    private static CountDownLatch countDownLatch = new CountDownLatch(10);

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    //消费者，取订单
    public static void consumerDelayMessage() {
        Jedis jedis = RedisDelayTest.getJedis();
        while (true) {
            Set<Tuple> order = jedis.zrangeWithScores(KEY, 0, 0);
            if (order == null || order.isEmpty()) {
                System.out.println("当前没有等待的任务");
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            Tuple tuple = (Tuple) order.toArray()[0];
            double score = tuple.getScore();
            Calendar instance = Calendar.getInstance();
            long nowTime = instance.getTimeInMillis() / 1000;
            if (nowTime >= score) {
                String element = tuple.getElement();
                Long orderId = jedis.zrem(KEY, element);
                if (orderId > 0) {
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":redis消费了一个任务：消费的订单OrderId为" + element);
                }
            }
        }
    }

    public static void main(String[] args) {
        RedisDelayTest delay = new RedisDelayTest();
        delay.productionDelayMessage();
        for (int i = 0; i < 10; i++) {
            new Thread(new DelayMessage()).start();
            countDownLatch.countDown();
        }
    }

    /**
     * 生产者,生成5个订单
     */
    public void productionDelayMessage() {
        for (int i = 0; i < 5; i++) {
            Calendar instance = Calendar.getInstance();
            // 3秒后执行
            instance.add(Calendar.SECOND, 3 + i);
            RedisDelayTest.getJedis().zadd(KEY, (instance.getTimeInMillis()) / 1000,
                    StringUtils.join("000000000", i + 1));
            System.out.println("生产订单: " + StringUtils.join("000000000", i + 1)
                    + " 当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            System.out.println((3 + i) + "秒后执行");
        }
    }

    static class DelayMessage implements Runnable {
        @Override
        public void run() {
            try {
                countDownLatch.await();
                consumerDelayMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
