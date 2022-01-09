package com.lchcommunity.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lchcommunity.community.dto.AccessTokenDTO;
import com.lchcommunity.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {
    //将AccessTokenDTO发送到github 获取token post
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
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

    //将accessToken发送到github 获取到githubUser信息 get
    public GithubUser getGithubUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String s = response.body().string();
            System.out.println(s);
            GithubUser githubUser = JSON.parseObject(s, GithubUser.class);
            return githubUser;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getGiteeAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://gitee.com/oauth/token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String s = response.body().string();
            JSONObject json = JSON.parseObject(s);
            String token = json.get("access_token").toString();
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getGiteeUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://gitee.com/api/v5/user?access_token="+accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String s = response.body().string();
            System.out.println(s);
            GithubUser giteeUser = JSON.parseObject(s, GithubUser.class);
            return giteeUser;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
