package com.example.blogsystem.common;

import java.util.UUID;

public class UserIdUtils {
    public static String getUserId(){
        //产生UUID
        String userid=UUID.randomUUID().toString().replace("-", "");
        return userid;
    }
}
