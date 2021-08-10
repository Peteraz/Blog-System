package com.example.blogsystemconsumer.controller;

import com.example.blogsystemconsumer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    //@Autowired是用在JavaBean中的注解，通过byType形式，用来给指定的字段或方法注入所需的外部资源。@Autowired是优先根据类型装配。
    @Autowired
    private ProductService productService;

    @RequestMapping(value="getConsumer")
    public String getConsumer(){
        String str=productService.getProduct();
        return str;
    }
}