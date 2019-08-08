package com.example.demo.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.Date;

/**
 * <pre>
 * 如果今天下午 3 点前进行下单，那么发货时间是明天，
 * 如果今天下午 3 点后进行下单，那么发货时间是后天，
 * 如果被确定的时间是周日，那么在此时间上再加 1 天为发货时间。
 * </pre>
 *
 * @author zhou.xy
 * @date 2019/8/8
 * @since 1.0
 */
public class DateTimeUtil {
    public static final DateTime DISTRIBUTION_TIME_SPLIT_TIME = new DateTime().withTime(15, 0, 0, 0);

    public static Date calculateDistributionTimeByOrderCreateTime(Date orderCreateTime) {
        DateTime orderCreateDateTime = new DateTime(orderCreateTime);
        Date tomorrow = orderCreateDateTime.plusDays(1).toDate();
        Date theDayAfterTomorrow = orderCreateDateTime.plusDays(2).toDate();
        return orderCreateDateTime.isAfter(DISTRIBUTION_TIME_SPLIT_TIME) ?
                wrapDistributionTime(theDayAfterTomorrow) : wrapDistributionTime(tomorrow);
    }

    public static Date wrapDistributionTime(Date distributionTime) {
        DateTime currentDistributionDateTime = new DateTime(distributionTime);
        DateTime plusOneDay = currentDistributionDateTime.plusDays(1);
        boolean isSunday = (DateTimeConstants.SUNDAY == currentDistributionDateTime.getDayOfWeek());
        return isSunday ? plusOneDay.toDate() : currentDistributionDateTime.toDate();
    }

    public static void main(String[] args) {
        System.out.println(calculateDistributionTimeByOrderCreateTime(new Date()));
    }
}
