package com.example.blogsystemconsumer;

import com.example.blogsystemconsumer.controller.ConsumerController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BlogSystemConsumerApplicationTests {
    @Resource
    ConsumerController consumerController;

    @Test
    void contextLoads() {
        String account = "Peter";
        String password = "123456";
        String result = consumerController.login(account, password);
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("something wrong");
        }
    }
}
