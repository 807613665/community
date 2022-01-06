package com.lchcommunity.community.interceptor;

import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessinoInterceptor implements HandlerInterceptor {
    @Autowired
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //使用cookies判断是否已经登录
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0)
            for (Cookie i : cookies) {
                if (i.getName().equals("token")) {
                    User user = userMapper.cheackToken(i.getValue());
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
