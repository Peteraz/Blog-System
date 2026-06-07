package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystemconsumer.service.MailProviderService;
import org.springframework.stereotype.Component;

@Component
public class BackendMailProviderService implements MailProviderService {
    @Override
    public String sendMail(String email) {
        return JsonUtils.jsonPrint(0, "发送错误!", null);
    }
}
