package com.example.blogsystemmailprovider.service;

import com.example.blogsystem.common.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class SendMailService {
    @Autowired
    private JavaMailSender mailSender;        //邮件发送接口
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")  //读取配置文件中的参数
    private String Sender;            //发送人
    private String receiver="673840304@qq.com";  //收件人
    private String loaclhost="http://localhost:7040/mail-provider/?token=";

    public void SendSimpleMail () throws Exception{
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(Sender);
        mailMessage.setTo(receiver);
        mailMessage.setSubject("这是一封简单的邮件！");
        mailMessage.setText("Hello World!今天天气真好！");
        mailSender.send(mailMessage);
    }

    public void SendAttchmentsMail() throws Exception{
        MimeMessage mimeMailMessage=mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setFrom(Sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("这是一封有附件的邮件");
        mimeMessageHelper.setText("附件就是两张图片，请你看看:");
        /*FileSystemResource file=new FileSystemResource(new File("D:\\Photos\\\forest.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg",file);*/
        mimeMessageHelper.addAttachment("forest.jpg",new File("D:\\Photos\\forest.jpg"));
        mimeMessageHelper.addAttachment("sun.jpg",new File("D:\\Photos\\sun.jpg"));
        mailSender.send(mimeMailMessage);
    }

    public void SendInlineMail() throws Exception{
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(Sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("这是一封有附件的邮件，请你仔细看看！");
        mimeMessageHelper.setText("<html><body><img src=\"cid:forest\"></body></html>",true);
        mimeMessageHelper.addInline("forest",new File("D:\\Photos\\forest.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg",new File("D:\\Photos\\forest.jpg"));
        mailSender.send(mimeMessage);
    }
    public void SendMail() throws Exception{
        /*User user= (User)session.getAttribute("user");*/
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(Sender);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("博客密码修改");
        Context context=new Context();
        context.setVariable("username",Sender);
        context.setVariable("link",loaclhost+UUIDUtils.getToken());
        String emailContent=templateEngine.process("/mail",context);
        mimeMessageHelper.setText(emailContent,true);
        mailSender.send(mimeMessage);
    }
}