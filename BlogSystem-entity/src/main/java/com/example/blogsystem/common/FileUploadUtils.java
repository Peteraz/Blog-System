package com.example.blogsystem.common;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

public class FileUploadUtils {
    public static String Upload( MultipartFile file){
        //文件名字
        String fileName=file.getOriginalFilename();
        //副档名
        String suffixName=fileName.substring(fileName.lastIndexOf("."));
        //上传的路径
        String filePath="H:\\Repository\\SpringCloud\\BlogSystem\\BlogSystem-consumer\\src\\main\\resources\\static\\img\\photos";
        //新文件名字
        fileName= UUID.randomUUID().toString().replace("-","")+fileName;
        File dest=new File(filePath,fileName);
        //不存的话在创建一个文件夹
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try{
            file.transferTo(dest);
        }catch(Exception e){
            e.printStackTrace();
        }
        String filename="H:\\Repository\\SpringCloud\\BlogSystem\\BlogSystem-consumer\\src\\main\\resources\\static\\img\\photos"+fileName;
        return filename;
    }

    public static String IsImg(MultipartFile file){
        if(file.isEmpty()){
            System.out.print("文件是空的");
            return "null";
        }
        if(file.getSize()>5*1024*1024){
            return "too big";
        }else{
            String fileType= file.getContentType();;
            if(fileType.equals("image/jpeg") || fileType.equals("image/png") || fileType.equals("image/bmp") || fileType.equals("image/gif") || fileType.equals("image/raw")){
                return "yes";
            }else{
                return "no";
            }
        }
    }
}