package com.lyh.api.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * @author shoushen.luan
 * @since 2022-10-23
 */
public class DateUtilsTest {
    public static void main(String[] args) {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("now:" + format.format(date));
        date = DateUtils.addYears(new Date(), 1);
        System.out.println("添加1年:" + format.format(date));
        date = DateUtils.addMonths(date, 1);
        System.out.println("添加1月:" + format.format(date));
        date = DateUtils.addWeeks(date, 1);
        System.out.println("添加1周:" + format.format(date));
        date = DateUtils.addDays(date, 1);
        System.out.println("添加1日:" + format.format(date));
        date = DateUtils.addHours(date, 1);
        System.out.println("添加小时:" + format.format(date));
        date = DateUtils.addMinutes(date, 1);
        System.out.println("添加分钟:" + format.format(date));
        date = DateUtils.addSeconds(date, 5);
        System.out.println("添加5秒:" + format.format(date));
        date = DateUtils.addMilliseconds(date, 5000);
        System.out.println("添加5000毫秒:" + format.format(date));
        System.out.println("-----------------");
        date = DateUtils.setYears(date, 2020);
        System.out.println("设置2020年:" + format.format(date));
        date = DateUtils.setMonths(date, 2);
        System.out.println("设置2月:" + format.format(date));
        date = DateUtils.setDays(date, 18);
        System.out.println("设置18日:" + format.format(date));
        date = DateUtils.setHours(date, 10);
        System.out.println("设置10时:" + format.format(date));
        date = DateUtils.setMinutes(date, 20);
        System.out.println("设置20分:" + format.format(date));
        date = DateUtils.setSeconds(date, 59);
        System.out.println("设置59秒:" + format.format(date));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSSXXX").format(date));
        date = DateUtils.setMilliseconds(date, 888);
        System.out.println("设置毫秒值:");
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSSXXX").format(date));
    }

    @Test
    public void test() {
        Date now = new Date();
        Date tomorrow = DateUtils.addDays(now, 1);
        Date today = DateUtils.addDays(tomorrow, -1);
        Assertions.assertThat(today.toInstant().atZone(ZoneId.systemDefault()).get(ChronoField.DAY_OF_MONTH))
                .isEqualTo(now.toInstant().atZone(ZoneId.systemDefault()).get(ChronoField.DAY_OF_MONTH));

    }

}
