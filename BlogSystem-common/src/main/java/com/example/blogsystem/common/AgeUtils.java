package com.example.blogsystem.common;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AgeUtils {

    private AgeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getAgeDetail(String date) {
        //如果有空格
        int index = date.indexOf(" ");
        if (index != -1) {
            date = date.substring(0, index);
        }
        //分隔符
        String[] data = date.split("-");
        Calendar birthday = new GregorianCalendar(Integer.valueOf(data[0]), Integer.valueOf(data[1]), Integer.valueOf(data[2]));
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
        //月份从0开始计算，所以需要+1
        int month = now.get(Calendar.MONTH) + 1 - birthday.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        if (day < 0) {
            month -= 1;
            now.add(Calendar.MONTH, -1);
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (month < 0) {
            month = (month + 12) % 12;
            year--;
        }

        StringBuilder tag = new StringBuilder();
        if (year > 0) {
            tag.append(year).append("岁");
        }
        if (month > 0) {
            tag.append(month).append("个月");
        }
        if (day > 0) {
            tag.append(day).append("天");
        }
        if (year == 0 && month == 0 && day == 0) {
            tag.append("今日出生");
        } else if (year < 0) {
            tag.append("还未出生，出生日期有问题！");
        }
        return String.valueOf(tag);
    }
}