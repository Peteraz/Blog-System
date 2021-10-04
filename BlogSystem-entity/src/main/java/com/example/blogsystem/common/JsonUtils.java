package com.example.blogsystem.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.blogsystem.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonUtils {

    //map转换成Json字符串传回前端
    public static String jsonPrint(Integer status,String msg,Object data){
        ObjectMapper mapper=new ObjectMapper();
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
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

    //Jsonc字符串
    public static User UserObject(String json){
        User user=new User();
        user= JSON.parseObject(json, User.class);
        return user;
    }
}
