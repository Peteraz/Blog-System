package com.example.blogsystemmailprovider;

import com.example.blogsystemmailprovider.service.SendMailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogSystemMailProviderApplicationTests {
    @Autowired
    private SendMailService sendMailService;

    @Test
    void contextLoads() {
        try {
            sendMailService.sendInlineMail();
            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("邮件发送失败！");
        }
    }
}
