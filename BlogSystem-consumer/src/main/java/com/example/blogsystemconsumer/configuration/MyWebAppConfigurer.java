package com.example.blogsystemconsumer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //addResourceHandler虚拟路径,addResourceLocations可以是物理路径也可以不是
        registry.addResourceHandler("/static/img/photos/**").addResourceLocations("file:/Repository/BlogSystem/BlogSystem-consumer/src/main/resources/static/img/photos/");
    }
}
