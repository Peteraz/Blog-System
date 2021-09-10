package com.example.blogsystemconsumer;

import org.junit.jupiter.api.Test;
import com.example.blogsystemconsumer.controller.ConsumerController;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;

@SpringBootTest
class BlogSystemConsumerApplicationTests {
    @Resource
    ConsumerController consumerController;

    @Test
    void contextLoads() {
        String account="Peter";
        String password="123456";
        String result=consumerController.Login(account,password);
        if(result!=null){
            System.out.println(result);
        }else{
            System.out.println("something worng");
        }
    }
}
