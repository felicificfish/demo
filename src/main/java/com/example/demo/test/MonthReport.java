package com.example.demo.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

/**
 * 月报数据处理
 *
 * @author zhou.xy
 * @since 1.0.0
 */
@Log4j2
public class MonthReport {
    private static void sexData(String sex) {
        JSONObject obj = JSON.parseObject(sex);
        BigDecimal sex1 = obj.getBigDecimal("sex1");
        BigDecimal sex2 = obj.getBigDecimal("sex2");
        BigDecimal total = sex1.add(sex2);
        if (null == total || BigDecimal.ZERO.compareTo(total) == 0) {
            total = BigDecimal.ONE;
        }
        BigDecimal sex1Percent = sex1.multiply(BigDecimal.valueOf(100)).divide(total, 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal sex2Percent = sex2.multiply(BigDecimal.valueOf(100)).divide(total, 4, BigDecimal.ROUND_HALF_UP);
        log.info(sex1Percent + " | " + sex2Percent);
    }

    private static void ageData(String age) {
        JSONArray array = JSON.parseArray(age);
        DataHandler dataHandler = new DataHandler(array, 4).invoke();
        log.info(dataHandler.getD1Percent() + " | " + dataHandler.getD2Percent()
                + " | " + dataHandler.getD3Percent() + " | " + dataHandler.getD4Percent()
                + " | " + dataHandler.getD5Percent());
    }

    private static void termData(String term) {
        JSONArray array = JSON.parseArray(term);
        DataHandler dataHandler = new DataHandler(array, 6).invoke();
        log.info(dataHandler.getD1Percent() + " | " + dataHandler.getD2Percent()
                + " | " + dataHandler.getD3Percent() + " | " + dataHandler.getD4Percent()
                + " | " + dataHandler.getD5Percent());
    }

    public static void main(String[] args) {
        String sex = "{\"sex1\":0.00,\"sex2\":0.00}";
        sexData(sex);
        String age = "[{\"num\":0,\"type\":1},{\"num\":0,\"type\":2},{\"num\":0,\"type\":3},{\"num\":0,\"type\":4},{\"num\":0,\"type\":5}]";
        ageData(age);
        String term = "[{\"num\":0.00,\"type\":1},{\"num\":0.00,\"type\":2},{\"num\":0.00,\"type\":3},{\"num\":0.00,\"type\":4},{\"num\":0.00,\"type\":5}]";
        termData(term);
    }

    private static class DataHandler {
        private JSONArray array;
        private Integer scale;
        private BigDecimal d1Percent;
        private BigDecimal d2Percent;
        private BigDecimal d3Percent;
        private BigDecimal d4Percent;
        private BigDecimal d5Percent;

        private DataHandler(JSONArray array, Integer scale) {
            this.array = array;
            this.scale = scale;
        }

        private BigDecimal getD1Percent() {
            return d1Percent;
        }

        private BigDecimal getD2Percent() {
            return d2Percent;
        }

        private BigDecimal getD3Percent() {
            return d3Percent;
        }

        private BigDecimal getD4Percent() {
            return d4Percent;
        }

        private BigDecimal getD5Percent() {
            return d5Percent;
        }

        private DataHandler invoke() {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal d1 = BigDecimal.ZERO;
            BigDecimal d2 = BigDecimal.ZERO;
            BigDecimal d3 = BigDecimal.ZERO;
            BigDecimal d4 = BigDecimal.ZERO;
            BigDecimal d5 = BigDecimal.ZERO;
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                BigDecimal num = obj.getBigDecimal("num");
                total = total.add(num);
                switch (obj.getInteger("type")) {
                    case 1:
                        d1 = d1.add(num);
                        break;
                    case 2:
                        d2 = d2.add(num);
                        break;
                    case 3:
                        d3 = d3.add(num);
                        break;
                    case 4:
                        d4 = d4.add(num);
                        break;
                    case 5:
                        d5 = d5.add(num);
                        break;
                    default:
                }
            }
            if (null == total || BigDecimal.ZERO.compareTo(total) == 0) {
                total = BigDecimal.ONE;
            }
            d1Percent = d1.multiply(BigDecimal.valueOf(100)).divide(total, scale, BigDecimal.ROUND_HALF_UP);
            d2Percent = d2.multiply(BigDecimal.valueOf(100)).divide(total, scale, BigDecimal.ROUND_HALF_UP);
            d3Percent = d3.multiply(BigDecimal.valueOf(100)).divide(total, scale, BigDecimal.ROUND_HALF_UP);
            d4Percent = d4.multiply(BigDecimal.valueOf(100)).divide(total, scale, BigDecimal.ROUND_HALF_UP);
            d5Percent = d5.multiply(BigDecimal.valueOf(100)).divide(total, scale, BigDecimal.ROUND_HALF_UP);
            return this;
        }
    }
}
