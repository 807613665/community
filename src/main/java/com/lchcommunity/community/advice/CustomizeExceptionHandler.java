package com.lchcommunity.community.advice;

import com.alibaba.fastjson.JSON;
import com.lchcommunity.community.dto.ResultDTO;
import com.lchcommunity.community.exception.CustomizeErrorCode;
import com.lchcommunity.community.exception.CustomizeException;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//CustomizeExceptionHandler用于业务异常（在CRUD中出现的异常）
//CustomizeErrorController用于默认异常
@ControllerAdvice
public class CustomizeExceptionHandler extends ResponseEntityExceptionHandler {
    //错误页面跳转
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {

        String contentType = request.getContentType();
        //判断请求类型是json还是html
        if("application/json".equals(contentType)){
            //是json就使用writer将数据写到前端
            ResultDTO resultDTO = null;
            if(e instanceof CustomizeException){
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            }else{
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try{
                response.setContentType("application/json");//编码格式json
                response.setStatus(200);//服务器响应的状态码
                response.setCharacterEncoding("UTF-8");//编码，不设置会乱码
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            }catch (IOException ioe){

            }
            return null;
        }else{
            //将message传到error.html中
            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else{
                //System.out.println(e.getMessage());
                model.addAttribute("message",CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
