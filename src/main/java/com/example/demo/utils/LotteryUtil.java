package com.example.demo.utils;

import com.example.demo.model.GiftDO;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 抽奖工具类<br><br>
 * <p>
 * 整体思想：<br>
 * 奖品集合 + 概率比例集合<br>
 * 将奖品按集合中顺序概率计算成所占比例区间，放入比例集合。并产生一个随机数加入其中，排序。<br>
 * 排序后，随机数落在哪个区间，就表示那个区间的奖品被抽中。<br>
 * 返回的随机数在集合中的索引，该索引就是奖品集合中的索引。<br>
 * 比例区间的计算通过概率相加获得。
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
public class LotteryUtil {

    /**
     * 抽奖
     *
     * @param giftList 奖品集合
     * @return int 索引
     */
    public static int lottery(List<GiftDO> giftList) {
        if (!CollectionUtils.isEmpty(giftList)) {
            List<Double> orgProbList = new ArrayList<>(giftList.size());
            for (GiftDO gift : giftList) {
                // 按顺序将概率添加到集合中
                orgProbList.add(gift.getProbability());
            }
            return drawGift(orgProbList);
        }
        return -1;
    }

    /**
     * 抽奖
     *
     * @param giftProbList 奖品概率集合
     * @return int 索引
     */
    private static int drawGift(List<Double> giftProbList) {
        List<Double> sortRateList = new ArrayList<>();
        // 计算概率总和
        Double sumRate = 0D;
        for (Double prob : giftProbList) {
            sumRate += prob;
        }

        if (sumRate != 0) {
            // 概率所占比例
            double rate = 0D;
            for (Double prob : giftProbList) {
                rate += prob;
                // 构建一个比例区段组成的集合(避免概率和不为1)
                sortRateList.add(rate / sumRate);
            }

            // 随机生成一个随机数，并排序
            double random = Math.random();
            sortRateList.add(random);
            Collections.sort(sortRateList);

            // 返回该随机数在比例集合中的索引
            return sortRateList.indexOf(random);
        }
        return -1;
    }
}
