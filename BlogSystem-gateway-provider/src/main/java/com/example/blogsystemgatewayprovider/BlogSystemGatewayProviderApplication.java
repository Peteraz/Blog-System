package com.example.blogsystemgatewayprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BlogSystemGatewayProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSystemGatewayProviderApplication.class, args);
    }

}
