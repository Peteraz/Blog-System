package com.example.blogsystemproduct.controller;

import com.blogsystem.entity.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController注解，相当于@Controller+@ResponseBody两个注解的结合，返回json数据不需要在方法前面加@ResponseBody注解了，
// 但使用@RestController这个注解，就不能返回jsp,html页面，视图解析器无法解析jsp,html页面
@RestController
public class ProductController {
    @RequestMapping(value="getProduct")
    public String getProduct(){
        Product product=new Product();
        return product.toString();
    }
}