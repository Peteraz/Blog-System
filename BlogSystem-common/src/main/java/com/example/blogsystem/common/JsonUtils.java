package com.example.blogsystem.common;

import com.alibaba.fastjson2.JSON;

import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    //map转换成Json字符串传回前端
    public static String jsonPrint(Integer status, String msg, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("msg", msg);
        map.put("data", data);
        //System.out.println(JSON.toJSONString(map));
        return JSON.toJSONString(map);
    }

    //map转换成Json字符串传回前端
    public static String jsonPrint(Integer errno, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("errno", errno);
        map.put("data", data);
        JSON.toJSON(map);
        //System.out.println(JSON.toJSONString(map));
        return JSON.toJSONString(map);
    }
}
