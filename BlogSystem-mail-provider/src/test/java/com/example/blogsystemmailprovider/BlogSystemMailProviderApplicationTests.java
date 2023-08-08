package com.example.blogsystemmailprovider;

import com.example.blogsystemmailprovider.service.SendMailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
class BlogSystemMailProviderApplicationTests {
    @Autowired
    private SendMailService sendMailService;

    @Test
    void contextLoads() throws MessagingException {
        sendMailService.sendInlineMail();
        System.out.println("邮件发送成功！");
    }
}
