package com.example.blogsystem.common;

import java.util.UUID;

public class UUIDUtils {
    public static String getUserId(){
        //产生UUID
        String userid=UUID.randomUUID().toString().replace("-", "");
        return userid;
    }

    public static String getId(){
        //产生UUID
        String id=UUID.randomUUID().toString().replace("-", "");
        return id;
    }

    public static String getToken(){
        String token=UUID.randomUUID().toString();
        return token;
    }
}
