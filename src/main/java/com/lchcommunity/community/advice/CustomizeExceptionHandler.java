package com.lchcommunity.community.advice;

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

//CustomizeExceptionHandler用于业务异常（在CRUD中出现的异常）
//CustomizeErrorController用于默认异常
@ControllerAdvice
public class CustomizeExceptionHandler extends ResponseEntityExceptionHandler {
    //拦截异常，传递异常信息到error.html中
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model) {
        if(e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }else{
            System.out.println(e.getMessage());
            model.addAttribute("message","请稍后重试~");
        }
        return new ModelAndView("error");
    }
}
