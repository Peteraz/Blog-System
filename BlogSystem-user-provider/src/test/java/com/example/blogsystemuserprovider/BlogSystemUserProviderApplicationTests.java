package com.example.blogsystemuserprovider;

import com.example.blogsystem.common.AgeUtil;
import com.example.blogsystemuserprovider.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class BlogSystemUserProviderApplicationTests {
    @Resource
    private UserController  userController;
    @Test
    void contextLoads() {
        /*Map<String,String> map=new HashMap<>();
        map.put("account", "Peterkuan");
        map.put("password", "12345678");
        map.put("email", "132456@qq.com");
        map.put("name", "Peter");
        map.put("age", "18");
        map.put("sex", "男");
        String result=userController.Register(map);
        System.out.println(result);*/
        String age=AgeUtil.getAgeDetail("2010/03/20");
        System.out.print(age);
    }
}
