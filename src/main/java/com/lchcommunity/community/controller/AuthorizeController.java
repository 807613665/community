package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.AccessTokenDTO;
import com.lchcommunity.community.dto.GithubUser;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.provider.GithubProvider;
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
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    //将application.properties中的数据写入变量中
    @Value("${github.setClient.id}")
    private String setClientId;

    @Value("${github.setClient.secret}")
    private String setClientSecret;

    @Value("${github.setRedirect.uri}")
    private String setRedirectUri;

    @Autowired
    private UserMapper userMapper;

    //进行github登录认证
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        //将AccessTokenDTO发送到github 获取token  将数据写入到AccessTokenDTO中
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(setClientId);
        accessTokenDTO.setClient_secret(setClientSecret);
        accessTokenDTO.setRedirect_uri(setRedirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //获取到用户信息
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);

        System.out.println(githubUser.toString());

        if (githubUser.getId() != null) {
            //登录成功，写cookies
            request.getSession().setAttribute("user", githubUser);
            User user = new User();
            user.setName(githubUser.getName());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarurl());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            response.addCookie(new Cookie("token", token));
            //将用户写入数据库中
            userMapper.insert(user);
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }

    }
}
