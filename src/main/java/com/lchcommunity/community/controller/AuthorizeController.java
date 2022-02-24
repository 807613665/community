package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.AccessTokenDTO;
import com.lchcommunity.community.dto.GithubUser;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.provider.GithubProvider;
import com.lchcommunity.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
//日志
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    //将application.properties中的数据写入变量中
    @Value("${github.client.id}")
    private String githubClientId;

    @Value("${github.client.secret}")
    private String githubClientSecret;

    @Value("${github.redirect.uri}")
    private String githubRedirectUri;

    @Value("${gitee.client.id}")
    private String giteeClientId;

    @Value("${gitee.client.secret}")
    private String giteeClientSecret;

    @Value("${gitee.redirect.uri}")
    private String giteeRedirectUri;


    @Autowired
    private UserService userService;

    @GetMapping("/callback/gitee")
    public String callback(@RequestParam(name = "code") String code,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        //将AccessTokenDTO发送到github 获取token  将数据写入到AccessTokenDTO中
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(giteeClientId);
        accessTokenDTO.setClient_secret(giteeClientSecret);
        accessTokenDTO.setRedirect_uri(giteeRedirectUri);
        String accessToken = githubProvider.getGiteeAccessToken(accessTokenDTO);
        //获取到用户信息
        GithubUser giteeUser = githubProvider.getGiteeUser(accessToken);

        System.out.println(giteeUser.toString());

        if (giteeUser != null && giteeUser.getId() != null) {
            //登录成功，写cookies
            request.getSession().setAttribute("user", giteeUser);
            User user = new User();
            user.setName(giteeUser.getName());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(giteeUser.getAvatarurl());
            user.setAccountId(String.valueOf(giteeUser.getId()));

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            cookie.setPath("/");
            response.addCookie(cookie);
            //更新用户信息或插入新用户
            userService.updateOrInsert(user);
            return "redirect:/";
        } else {
            log.error("callback get User error {}",giteeUser);
            //登录失败
            return "redirect:/";
        }

    }

    //进行github登录认证
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        //将AccessTokenDTO发送到github 获取token  将数据写入到AccessTokenDTO中
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(githubClientId);
        accessTokenDTO.setClient_secret(githubClientSecret);
        accessTokenDTO.setRedirect_uri(githubRedirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //获取到用户信息
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);

        System.out.println(githubUser.toString());

        if (githubUser != null && githubUser.getId() != null) {
            //登录成功，写cookies
            request.getSession().setAttribute("user", githubUser);
            User user = new User();
            user.setName(githubUser.getName());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarurl());
            user.setAccountId(String.valueOf(githubUser.getId()));

            response.addCookie(new Cookie("token", token));
            //更新用户信息或插入新用户
            userService.updateOrInsert(user);
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        //删除Session
        request.getSession().removeAttribute("user");
        //删除Cookies
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
