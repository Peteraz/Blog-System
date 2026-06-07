package com.example.blogsystemconsumer.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    @Value("${blog.upload.dir:}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from the same configurable upload directory used by FileUploadUtils.
        registry.addResourceHandler("/static/img/photos/**").addResourceLocations("file:" + getUploadDir() + "/");
    }

    private String getUploadDir() {
        String resolvedUploadDir = uploadDir;
        if (StringUtils.isBlank(resolvedUploadDir)) {
            resolvedUploadDir = System.getenv("BLOG_UPLOAD_DIR");
        }
        if (StringUtils.isBlank(resolvedUploadDir)) {
            resolvedUploadDir = Paths.get(System.getProperty("user.dir"), "uploads", "photos").toString();
        }
        return Paths.get(resolvedUploadDir).toAbsolutePath().normalize().toString().replace("\\", "/");
    }
}
