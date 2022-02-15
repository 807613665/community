package com.lchcommunity.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;


/*
* BasicErrorController可以用作自定义的基类ErrorController。如果您想为新的内容类型添加处理程序，
* 这特别有用（默认是text/html专门处理并为其他所有内容提供后备）。为此，请扩展BasicErrorController，
* 添加一个 @RequestMapping具有produces属性的公共方法，并创建一个新类型的 bean。
* */

//如果存在自定义的ErrorController 则会使用自定义的，没有会使用默认的ErrorController
//重写ErrorController 可以将不同的状态码 转换为错误信息 传递到error.html中

//CustomizeExceptionHandler用于业务异常（在CRUD中出现的异常）
//CustomizeErrorController用于默认异常
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class CustomizeErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request, Model model) {
        HttpStatus status = this.getStatus(request);
        if(status.is4xxClientError()){
            model.addAttribute("message","你的请求出错了，请重新访问！");
        }
        if(status.is5xxServerError()){
            model.addAttribute("message","服务繁忙，请稍后重试！");
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
