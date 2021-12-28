package com.lchcommunity.community.provider;

import com.alibaba.fastjson.JSON;
import com.lchcommunity.community.dto.AccessTokenDTO;
import com.lchcommunity.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String s = response.body().string();
            System.out.println(s);
            return s.split("=")[1].split("&")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getGithubUser(String accessToken){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String s = response.body().string();
            System.out.println(s);
            GithubUser githubUser = JSON.parseObject(s,GithubUser.class);
            return githubUser;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
