package com.example.blogsystem.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtils {

    private FileUploadUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String realpath = "D://Repository//BlogSystem//BlogSystem-consumer//src//main//resources//static//img//photos//";

    private static final String virtualPath = "/consumer/static/img/photos/";

    /**
     * 中文正则表达式
     */
    private static final String REGEX_CHINESE = "[\u4e00-\u9fa5]";

    public static String upload(MultipartFile file) {
        //文件名字
        String fileName = file.getOriginalFilename();
        //副档名
        //String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //上传后的路径
        String filePath = realpath;
        if (null != fileName && !fileName.isEmpty()) {
            //去掉中文
            fileName = fileName.replaceAll(REGEX_CHINESE, "");
            //新文件名字
            fileName = UUID.randomUUID().toString().replace("-", "") + fileName;
            File dest = new File(filePath, fileName);
            //不存的话在创建一个文件夹
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);  //把内存图片写入磁盘中
            } catch (IOException e) {
                e.printStackTrace();
            }
            return virtualPath + fileName;
        } else {
            return null;
        }
    }

    public static String isImg(MultipartFile file) {
        if (file.isEmpty()) {
            System.out.print("文件是空的");
            return "null";
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            return "too big";
        } else {
            String fileType;
            if (StringUtils.isNotBlank(file.getContentType())) {
                fileType = file.getContentType();
                if (fileType.equals("image/jpeg") || fileType.equals("image/jpg") || fileType.equals("image/png") || fileType.equals("image/bmp") || fileType.equals("image/gif") || fileType.equals("image/raw")) {
                    return "yes";
                } else {
                    return "no";
                }
            }
        }
        return null;
    }
}