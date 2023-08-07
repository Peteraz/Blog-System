package com.example.blogsystem.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256加密
 */
public class SHA256Utils {

    private SHA256Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 利用java原生的类实现SHA256加密
     *
     * @param str
     * @return
     */
    public static String getSHA256(String str) {
        //java.security.MessageDigest类用于为应用程序提供信息摘要算法的功能，如MD5或SHA算法。简单点说就是用于生成散列码。
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String temp = null;
        for (byte aByte : bytes) {
            //0xFF是16进制的表达方式,F是15,十进制为：255,二进制为：1111 1111,与运算为是负数也不会改变二进制补码
            //因为byte要转化为int的时候,高的24位必然会补1,这样其二进制补码已经不一致了,&0xff可以将高的24位置为0,低8位保持原样
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位得进行补0操作
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }
}
