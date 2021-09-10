package com.example.blogsystemuserprovider;

import com.example.blogsystemuserprovider.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;

@SpringBootTest
class BlogSystemUserProviderApplicationTests {
    @Resource
    private UserController  userController;
    @Test
    void contextLoads() {
        String account="Peter";
        String password="123456";
        String result=userController.Login(account,password);
        if(result!=null){
            System.out.println(result);
        }else{
            System.out.println("something worng");
        }
    }
}
