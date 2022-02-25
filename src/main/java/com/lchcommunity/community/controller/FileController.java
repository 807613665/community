package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.FileDTO;
import com.lchcommunity.community.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileController {
    @Autowired
    FileService fileService;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {

//        FileDTO fileDTOs = new FileDTO();
//        fileDTOs.setUrl("/upload/images/1.mp4");
//        fileDTOs.setSuccess(1);
//        return fileDTOs;

        //对request强转 获取其中的MultipartFile
        //或者使用@RequestParam(value = "editormd-image-file", required = true) MultipartFile file
        MultipartHttpServletRequest multipartFile = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartFile.getFile("editormd-image-file");

        String upload = fileService.upload(file);
        FileDTO fileDTO = new FileDTO();
        if(upload!=null){
            fileDTO.setSuccess(1);
            fileDTO.setUrl(upload);
        }else{
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
        }
        return fileDTO;
    }
}
