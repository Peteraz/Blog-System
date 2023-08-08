package com.example.blogsystemmailprovider.service.impl;

import com.example.blogsystem.common.JsonUtils;
import com.example.blogsystem.common.UUIDUtils;
import com.example.blogsystemmailprovider.service.SendMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.concurrent.TimeUnit;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender mailSender;  //邮件发送接口

    @Autowired
    private TemplateEngine templateEngine;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.mail.username}")       //读取配置文件中的参数
    private String sender;                //发送人

    private String receiver = "xxxxxx@qq.com";  //收件人

    private String localHost = "http://localhost:9001/consumer/getResetPassword?token=";

    Logger logger = LoggerFactory.getLogger(SendMailServiceImpl.class);

    @Override
    public void sendSimpleMail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(receiver);
        mailMessage.setSubject("这是一封简单的邮件！");
        mailMessage.setText("Hello World!今天天气真好！");
        mailSender.send(mailMessage);
    }

    @Override
    public void sendAttachmentsMail() throws MessagingException {
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("这是一封有附件的邮件");
        mimeMessageHelper.setText("附件就是两张图片，请你看看:");
        /*FileSystemResource file=new FileSystemResource(new File("D:\\Photos\\\forest.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg",file);*/
        mimeMessageHelper.addAttachment("forest.jpg", new File("D:\\Photos\\forest.jpg"));
        mimeMessageHelper.addAttachment("sun.jpg", new File("D:\\Photos\\sun.jpg"));
        mailSender.send(mimeMailMessage);
    }

    @Override
    public void sendInlineMail() throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("这是一封有附件的邮件，请你仔细看看！");
        mimeMessageHelper.setText("<html><body><img src=\"cid:forest\"></body></html>", true);
        mimeMessageHelper.addInline("forest", new File("D:\\WallPaper\\nature-flower-forest-flowers-ocean-river-sea-1501189-wallhere.com.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg", new File("D:\\WallPaper\\nature-flower-forest-flowers-ocean-river-sea-1501189-wallhere.com.jpg"));
        mailSender.send(mimeMessage);
    }

    @Override
    public String sendMail(String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("博客密码修改");
            Context context = new Context();
            context.setVariable("username", email);
            String token = UUIDUtils.getToken();
            context.setVariable("link", localHost + token);
            String emailContent = templateEngine.process("/mail", context);
            mimeMessageHelper.setText(emailContent, true);
            mailSender.send(mimeMessage);
            redisTemplate.opsForValue().set("resetPwdToken", token, 60 * 60 * 12, TimeUnit.SECONDS); //token
            logger.info(token);
            redisTemplate.opsForValue().set("resetEmail", email, 60 * 60 * 12, TimeUnit.SECONDS);    //接收用户
            return JsonUtils.jsonPrint(1, "邮件发送成功!", null);
        } catch (Exception e) {
            logger.error("send mail error: ", e);
            return JsonUtils.jsonPrint(0, e.getMessage(), null);
        }
    }

}
