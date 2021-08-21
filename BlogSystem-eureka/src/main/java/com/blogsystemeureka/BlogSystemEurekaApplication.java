package com.blogsystemeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//SpringBoot启动类
@SpringBootApplication
//这是一个eureka-server
@EnableEurekaServer
public class BlogSystemEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSystemEurekaApplication.class, args);
    }

}
