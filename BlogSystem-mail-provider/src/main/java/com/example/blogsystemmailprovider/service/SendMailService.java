package com.example.blogsystemmailprovider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.FileSystem;

@Service
public class SendMailService {
    @Autowired
    JavaMailSender mailSender;        //邮件发送接口
    @Value("${spring.mail.username}")  //读取配置文件中的参数
    private String Sender;            //发送人
    private String receiver="673840304@qq.com";  //收件人

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
        mimeMessageHelper.setSubject("这是一封有附件的邮件！");
        mimeMessageHelper.setText("附件就是两张图片:");
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
        mimeMessageHelper.setSubject("这是一封嵌入静态资源的邮件！");
        mimeMessageHelper.setText("<html><body><img src=\"cid:forest\"><img src=\"cid:sun\"></body></html>",true);
        /*FileSystemResource file=new FileSystemResource(new File("D:\\Photos\\\forest.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg",file);*/
        mimeMessageHelper.addInline("forest",new File("D:\\Photos\\forest.jpg"));
        mimeMessageHelper.addInline("sun",new File("D:\\Photos\\sun.jpg"));
        mimeMessageHelper.addAttachment("forest.jpg",new File("D:\\Photos\\forest.jpg"));
        mimeMessageHelper.addAttachment("sun.jpg",new File("D:\\Photos\\sun.jpg"));
        mailSender.send(mimeMessage);
    }
}
