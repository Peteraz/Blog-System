package com.example.blogsystemmailprovider.service;

import javax.mail.MessagingException;

public interface SendMailService {
    public void sendSimpleMail();

    public void sendAttachmentsMail() throws MessagingException;

    public void sendInlineMail() throws MessagingException;

    public String sendMail(String email);
}