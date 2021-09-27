package com.example.blogsystemmailprovider.service;

public interface SendMailService {
    public void SendSimpleMail ();

    public void SendAttchmentsMail();

    public void SendInlineMail();

    public void SendMail(String email);
}