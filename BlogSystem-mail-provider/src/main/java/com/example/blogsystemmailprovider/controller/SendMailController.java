package com.example.blogsystemmailprovider.controller;

import com.example.blogsystemmailprovider.service.SendMailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@RestController
public class SendMailController {

    @Resource
    private SendMailService sendMailService;

    @RequestMapping(value = "SendSimpleMail", method = RequestMethod.POST)
    public void sendSimpleMail() {
        sendMailService.sendSimpleMail();
    }

    @RequestMapping(value = "SendAttachmentsMail", method = RequestMethod.POST)
    public void sendAttachmentsMail() throws Exception {
        sendMailService.sendAttachmentsMail();
    }

    @RequestMapping(value = "SendInlineMail", method = RequestMethod.POST)
    public void sendInlineMail() throws MessagingException {
        sendMailService.sendInlineMail();
    }

    @RequestMapping(value = "SendMail", method = RequestMethod.POST)
    public String sendMail(@RequestParam("email") String email) {
        return sendMailService.sendMail(email);
    }
}