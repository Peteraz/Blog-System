package com.example.blogsystemconsumer;

import com.example.blogsystemconsumer.controller.ConsumerController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SpringBootTest
class BlogSystemConsumerApplicationTests {
    @Resource
    ConsumerController consumerController;

    @Test
    void contextLoads() {
        String account="Peter";
        String password="123456";
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String result=consumerController.Login(account,password);
        System.out.println(result);
    }
}
