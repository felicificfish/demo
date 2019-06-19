package com.example.demo.utils;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 按几率产生随机数<br>
 * 例如，产生0.1-100的随机数，0.1-1的几率是90%，1-10的几率是9%，10-100的几率是1%
 *
 * @author zhou.xy
 * @since 2019/6/19
 */
public class RateRandomNumber {

    /**
     * 产生随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return [min, max)
     */
    public static double produceRandomDouble(double min, double max) {
        return RandomUtils.nextDouble(min, max);
    }

    /**
     * 产生随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return [min, max)
     */
    public static int produceRandomInt(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }

    public static double produceRateRandomNumber(double min, double max, Rate rate) {
        if (min > max) {
            throw new IllegalArgumentException("min值必须小于max值");
        }
        if (null == rate || CollectionUtils.isEmpty(rate.separates) || CollectionUtils.isEmpty(rate.percents)) {
            return produceRandomDouble(min, max);
        }
        if (rate.separates.size() + 1 != rate.percents.size()) {
            throw new IllegalArgumentException("分割数字的个数加1必须等于百分比个数");
        }
        int totalPercent = 0;
        for (Integer percent : rate.percents) {
            if (percent < 0 || percent > 100) {
                throw new IllegalArgumentException("百分比必须在[0,100]之间");
            }
            totalPercent += percent;
        }
        if (totalPercent != 100) {
            throw new IllegalArgumentException("百分比之和必须为100");
        }
        for (Double separate : rate.separates) {
            if (separate <= min || separate >= max) {
                throw new IllegalArgumentException("分割数值必须在(min,max)之间");
            }
        }
        // 例如：3个插值，可以将一个数值范围分割成4段
        int rangeCount = rate.separates.size() + 1;
        //构造分割的n段范围
        List<Range> ranges = new ArrayList<Range>();
        int scopeMax = 0;
        for (int i = 0; i < rangeCount; i++) {
            Range range = new Range();
            range.min = (i == 0 ? min : rate.separates.get(i - 1));
            range.max = (i == rangeCount - 1 ? max : rate.separates.get(i));
            range.percent = rate.percents.get(i);

            // 片段占比，转换为[1,100]区间的数字
            range.percentScopeMin = scopeMax + 1;
            range.percentScopeMax = range.percentScopeMin + (range.percent - 1);
            scopeMax = range.percentScopeMax;
            ranges.add(range);
        }
        // 结果赋初值
        double r = min;
        // [1,100]
        int randomInt = RandomUtils.nextInt(1, 101);
        for (int i = 0; i < ranges.size(); i++) {
            Range range = ranges.get(i);
            //判断使用哪个range产生最终的随机数
            if (range.percentScopeMin <= randomInt && randomInt <= range.percentScopeMax) {
                r = produceRandomDouble(range.min, range.max);
                break;
            }
        }
        return r;
    }

    public static void main(String[] args) {
        List<Double> separates = new ArrayList<>();
        separates.add(1.0);
        separates.add(10.0);
        List<Integer> percents = new ArrayList<>();
        percents.add(90);
        percents.add(9);
        percents.add(1);

        Rate rate = new Rate();
        rate.separates = separates;
        rate.percents = percents;

        int p1 = 0;
        int p2 = 0;
        int p3 = 0;
        for (int i = 0; i < 1000; i++) {
            double number = produceRateRandomNumber(0.1, 100, rate);
            if (0.1 <= number && number < 90) {
                p1++;
            } else if (90 <= number && number < 99) {
                p2++;
            } else {
                p3++;
            }
//            System.out.println(String.format("%.2f", number));
        }
        System.out.println("p1=" + p1 + ",p2=" + p2 + ",p3=" + p3);
    }

    public static class Rate {
        private List<Double> separates;
        private List<Integer> percents;
    }

    public static class Range {
        public double min;
        public double max;
        public int percent;

        /**
         * 百分比转换为[1,100]的数字的最小值
         */
        public int percentScopeMax;
        /**
         * 百分比转换为[1,100]的数字的最大值
         */
        public int percentScopeMin;
    }
}
