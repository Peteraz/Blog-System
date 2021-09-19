package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.UserProviderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class BackendUserProviderService implements UserProviderService {
    private static final String BACKEND="backendA";

    @Override
    @CircuitBreaker(name=BACKEND)
    public String Register(@RequestBody Map<String, String> map){
        return "连接超时,请稍后重试!";
    }

    @Override
    @CircuitBreaker(name=BACKEND)
    public String Login(@RequestParam("account") String account, @RequestParam("password") String password){
        return "连接超时,请稍后重试!";
    }

    @Override
    @CircuitBreaker(name=BACKEND)
    public String Logout(HttpSession session){
        return "连接超时,请稍后重试!";
    }

    @Override
    @CircuitBreaker(name=BACKEND)
    public String ForgetPWD(@RequestParam("account") String account){
        return "连接超时,请稍后重试!";
    }
}
