package com.lchcommunity.community.interceptor;

import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.model.UserExample;
import com.lchcommunity.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessinoInterceptor implements HandlerInterceptor {
    @Autowired
    UserMapper userMapper;
    @Autowired
    NotificationService notificationService;

    @Value("${github.redirect.uri}")
    private String githubRedirectUri;

    @Value("${gitee.redirect.uri}")
    private String giteeRedirectUri;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置 context 级别的属性
        request.getServletContext().setAttribute("giteeRedirectUri", giteeRedirectUri);
        request.getServletContext().setAttribute("githubRedirectUri", githubRedirectUri);
        //使用cookies判断是否已经登录
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0)
            for (Cookie i : cookies) {
                if (i.getName().equals("token")) {
                    String token = i.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> user = userMapper.selectByExample(userExample);
                    if (user.size() != 0) {
                        request.getSession().setAttribute("user", user.get(0));
                        //通知数量
                        Long unreadCount = notificationService.unreadCount(user.get(0).getId());
                        request.getSession().setAttribute("unreadCount", unreadCount);
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
