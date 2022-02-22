package com.lchcommunity.community.cache;

import com.lchcommunity.community.dto.TagDTO;

import java.util.*;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOList = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setTitle("开发语言");
        program.setTag(Arrays.asList("javascript", "php", "css", "html", "html5", "java", "node.js", "python", "c++", "c", "golang", "objective-c", "typescript", "shell", "swift", "c#", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagDTOList.add(program);

        TagDTO framework = new TagDTO();
        framework.setTitle("平台框架");
        framework.setTag(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOList.add(framework);


        TagDTO server = new TagDTO();
        server.setTitle("服务器");
        server.setTag(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos", "缓存 tomcat", "负载均衡", "unix", "hadoop", "windows-server"));
        tagDTOList.add(server);

        TagDTO db = new TagDTO();
        db.setTitle("数据库");
        db.setTag(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql memcached", "sqlserver", "postgresql", "sqlite"));
        tagDTOList.add(db);

        TagDTO tool = new TagDTO();
        tool.setTitle("开发工具");
        tool.setTag(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text", "xcode intellij-idea", "eclipse", "maven", "ide", "svn", "visual-studio", "atom emacs", "textmate", "hg"));
        tagDTOList.add(tool);
        return tagDTOList;
    }

    public static String filterInvalid(String tags) {
        String[] taglist = tags.split(",");
        List<TagDTO> tagDTOList = TagCache.get();
        List<String> okTag = new ArrayList<>();
        for (TagDTO i : tagDTOList) {
            for (String j : i.getTag()) {
                okTag.add(j);
            }
        }
        List<String> invalid = new ArrayList<>();
        for(String i:taglist){
            if(okTag.indexOf(i)==-1){
                return i;
            }
        }
        return "";
    }
}
