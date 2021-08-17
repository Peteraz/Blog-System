package com.blogsystem;

import java.util.UUID;

public class TestUUID {
    public static void main(String[] args) {
            String userid= UUID.randomUUID().toString().replace("-", "");
            System.out.println("UUID:"+userid);
    }
}
