package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BackendProductService implements FallbackFactory<ProductService> {
    private static final String BACKEND = "backendA";
    @Override
    public ProductService create(Throwable throwable){
        return new ProductService() {
            @Override
            @CircuitBreaker(name = BACKEND)
            public String getService() {
                return "连接超时,请稍后重试!";
            }
        };
    }

}
