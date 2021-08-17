package com.example.blogsystem.entity;

public class Consumer {
    private String name;
    private int age;
    private String add;
    private String email;

    public Consumer(){
        this.name="name";
        this.age=12;
        this.add="澳门历史互通";
        this.email="12345678@qq.com";
    }

    @Override
    public String toString(){
        return "Consumer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", add='" + add + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}