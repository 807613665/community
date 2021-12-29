package com.lchcommunity.community.controller;

import com.lchcommunity.community.dto.AccessTokenDTO;
import com.lchcommunity.community.dto.GithubUser;
import com.lchcommunity.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.setClient.id}")
    private String setClientId;
    @Value("${github.setClient.secret}")
    private String setClientSecret;
    @Value("${github.setRedirect.uri}")
    private String setRedirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(setClientId);
        accessTokenDTO.setClient_secret(setClientSecret);
        accessTokenDTO.setRedirect_uri(setRedirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);

        System.out.println(githubUser.toString());

        if (githubUser != null) {
            //登录成功，写cookies
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }

    }
}
