package com.example.blogsystemconsumer.hystrix;

import com.example.blogsystemconsumer.service.UserProviderService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.Map;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
@Component
public class HystrixUserClientService implements FallbackFactory<UserProviderService>{
    @Override
    public UserProviderService create(Throwable throwable) {
        return new UserProviderService() {
            @Override
            public String Register(@RequestBody Map<String,String> map){
                return "连接超时，稍后重试。";
            }

            @Override
            public String Login(@RequestParam("account") String account, @RequestParam("password") String password){
                return "连接超时，稍后重试。";
            }

            @Override
            public String Logout(HttpSession session){
                return "连接超时，稍后重试。";
            }

            @Override
            public String ForgetPWD(@RequestParam("account") String account){
                return "连接超时，稍后重试。";
            }
        };
    }
}
