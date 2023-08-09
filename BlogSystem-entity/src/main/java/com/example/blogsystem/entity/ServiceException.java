package com.example.blogsystem.entity;

import lombok.Data;

/***
 * 业务异常
 */
@Data
public class ServiceException extends RuntimeException {

    private int code;

    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}