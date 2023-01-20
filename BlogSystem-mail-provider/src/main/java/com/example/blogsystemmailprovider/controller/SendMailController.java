package com.example.blogsystemmailprovider.controller;

import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.common.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.concurrent.TimeUnit;

@RestController
public class SendMailController {
    @Autowired
    private JavaMailSender mailSender;        //邮件发送接口

    @Autowired
    private TemplateEngine templateEngine;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.mail.username}")  //读取配置文件中的参数
    private String Sender;            //发送人

    private String receiver = "673840304@qq.com";  //收件人

    private String loaclhost = "http://localhost:9001/consumer/getResetPassword?token=";

    @RequestMapping(value = "SendSimpleMail", method = RequestMethod.POST)
    public void SendSimpleMail() throws Exception {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(Sender);
        mailMessage.setTo(receiver);
        mailMessage.setSubject("这是一封简单的邮件！");
        mailMessage.setText("Hello World!今天天气真好！");
        mailSender.send(mailMessage);
    }

    @RequestMapping(value = "SendAttChmentsMail", method = RequestMethod.POST)
    public void SendAttChmentsMail() throws Exception {
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
        mimeMessageHelper.setFrom(Sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("这是一封有附件的邮件");
        mimeMessageHelper.setText("附件就是两张图片，请你看看:");
        /*FileSystemResource file=new FileSystemResource(new File("D:\\Photos\\\forest.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg",file);*/
        mimeMessageHelper.addAttachment("forest.jpg", new File("D:\\Photos\\forest.jpg"));
        mimeMessageHelper.addAttachment("sun.jpg", new File("D:\\Photos\\sun.jpg"));
        mailSender.send(mimeMailMessage);
    }

    @RequestMapping(value = "SendInlineMail", method = RequestMethod.POST)
    public void SendInlineMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(Sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("这是一封有附件的邮件，请你仔细看看！");
        mimeMessageHelper.setText("<html><body><img src=\"cid:forest\"></body></html>", true);
        mimeMessageHelper.addInline("forest", new File("D:\\Photos\\forest.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg", new File("D:\\Photos\\forest.jpg"));
        mailSender.send(mimeMessage);
    }

    @RequestMapping(value = "SendMail", method = RequestMethod.POST)
    public String SendMail(@RequestParam("email") String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(Sender);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("博客密码修改");
            Context context = new Context();
            context.setVariable("username", email);
            String token = UUIDUtils.getToken();
            context.setVariable("link", loaclhost + token);
            String emailContent = templateEngine.process("/mail", context);
            mimeMessageHelper.setText(emailContent, true);
            mailSender.send(mimeMessage);
            redisTemplate.opsForValue().set("resetPwdToken", token, 60 * 60 * 12, TimeUnit.SECONDS); //token
            System.out.println(token);
            redisTemplate.opsForValue().set("resetEmail", email, 60 * 60 * 12, TimeUnit.SECONDS);    //接收用户
            return JsonUtils.jsonPrint(1, "邮件发送成功!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }
}