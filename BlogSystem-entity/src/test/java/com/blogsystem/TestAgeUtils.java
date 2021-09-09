package com.blogsystem;

import com.example.blogsystem.common.AgeUtils;

public class TestAgeUtils {
    public static void main(String[] args) {
        String age= AgeUtils.getAgeDetail("1994/03/07");
        System.out.println("Age:"+age);
    }
}
