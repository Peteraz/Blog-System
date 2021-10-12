package com.example.blogsystemconsumer.service;

import com.example.blogsystemconsumer.resilience4j.BackendArticleProviderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
//@Componet注解最好加上，不加idea会显示有错误，但是不影响系统运行；
@Component
//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "article-provider-server",path="/article-provider",fallback = BackendArticleProviderService.class)
public interface ArticleProviderService {
    @RequestMapping(value="createArticle")//创建文章
    String createArticle(@RequestParam("articleName") String articleName, @RequestParam("articleContents") String articleContents);
}
