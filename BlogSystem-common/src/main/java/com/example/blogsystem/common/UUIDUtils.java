package com.example.blogsystem.common;

import java.util.UUID;

public class UUIDUtils {

    private UUIDUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getUserId() {
        //产生UUID
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getId() {
        //产生UUID
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getToken() {
        return UUID.randomUUID().toString();
    }
}
