package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.MailProviderService;
import org.springframework.stereotype.Component;

@Component
public class BackendMailProviderService implements MailProviderService {
    @Override
    public String SendMail(String email){
        return "发送错误!";
    }
}
