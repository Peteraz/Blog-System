package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component(value="BackendProductService")
public class BackendProductService implements ProductService {
    private static final String BACKEND="backendA";

    @Override
    @CircuitBreaker(name=BACKEND)
    public String getService(){
        return "连接超时！";
    }
}
