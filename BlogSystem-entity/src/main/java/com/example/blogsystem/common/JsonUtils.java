package com.example.blogsystem.common;

import com.alibaba.fastjson.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    //map转换成Json字符串传回前端
    public static String jsonPrint(Integer status,String msg,Object data){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("msg", msg);
        map.put("data", data);
        //System.out.println(JSONArray.toJSONString(map));
        return JSONArray.toJSONString(map);
    }

    //map转换成Json字符串传回前端
    public static String jsonPrint(Integer errno,Object data){
        Map<String, Object> map = new HashMap<>();
        map.put("errno", errno);
        map.put("data", data);
        //System.out.println(JSONArray.toJSONString(map));
        return JSONArray.toJSONString(map);
    }
}
