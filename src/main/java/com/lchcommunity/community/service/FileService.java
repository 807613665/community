package com.lchcommunity.community.service;

import com.lchcommunity.community.exception.CustomizeErrorCode;
import com.lchcommunity.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    //定义一个目标路径,就是我们要把图片上传到的位置
    @Value("${local.path}")
    private String localPath;
    //定义访问图片的路径
    @Value("${visit.path}")
    private String visitPath;
    public String upload(MultipartFile multipartFile){
        //获得上传文件的名称
        String filename = multipartFile.getOriginalFilename();
        //为了保证图片在服务器中名字的唯一性，这个使我们要用UUID来对filename来进行改写
        String uuid = UUID.randomUUID().toString().replace("-","");
        //将生成的UUID和filename进行拼接。
        String newFileName = uuid+"-"+filename;
        //创建一个文件实例对象
        File image = new File(localPath,newFileName);
        //对这个文件进行上传操作
        try {
            multipartFile.transferTo(image);
        } catch (IOException e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        //返回图片访问地址和名称
        return visitPath+newFileName;
    }
}
