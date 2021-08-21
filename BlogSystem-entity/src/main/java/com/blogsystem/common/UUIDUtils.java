package com.blogsystem.common;

import java.util.UUID;

public class UUIDUtils {
    public static String getUserId(){
        //产生UUID
        String userid=UUID.randomUUID().toString().replace("-", "");
        return userid;
    }

    public static String getToken(){
        String token=UUID.randomUUID().toString();
        return token;
    }
}
