package com.example.blogsystemmailprovider;

import com.example.blogsystemmailprovider.controller.SendMailController;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import javax.annotation.Resource;

@SpringBootTest
class BlogSystemMailProviderApplicationTests {
    @Resource
    private SendMailController sendMailController;

    @Test
    void contextLoads() {
        try{
            sendMailController.SendInlineMail();
            System.out.println("邮件发送成功！");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("邮件发送失败！");
        }
    }
}
