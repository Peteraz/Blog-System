package com.example.blogsystemproduct.controller;

import com.example.blogsystem.entity.Product;
import org.apache.tomcat.jni.Time;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

//@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RestController
public class ProductController {
    @RequestMapping(value="getProduct")
    public String getProduct(){
        Product product=new Product();
        return product.toString();
    }
    @RequestMapping(value="getService")
    public String getService(){
        Time.sleep(6000);
        String serverport="这是8001服务提供者,当你看到些字时候就是没问题了！";
        return serverport;
    }
}