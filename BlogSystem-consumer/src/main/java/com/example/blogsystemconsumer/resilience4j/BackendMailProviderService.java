package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.MailProviderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component(value="BackendMailProviderService")
public class BackendMailProviderService implements MailProviderService {
    private static final String BACKEND="backendA";

    @Override
    @CircuitBreaker(name=BACKEND)
    public String SendMail(Object object){
        return "发送错误!";
    }
}
