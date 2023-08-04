package com.example.blogsystemmailprovider.service;

public interface SendMailService {
    public void SendSimpleMail ();

    public void SendAttachmentsMail();

    public void SendInlineMail();

    public void SendMail(String email);
}