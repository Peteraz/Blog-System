package com.example.blogsystemconsumer.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenfeignConfigurer {
    @Bean
    Logger.Level openfeignLoggerLevel(){
        return Logger.Level.FULL;
    }
}