package com.lchcommunity.community.controller;

import com.lchcommunity.community.cache.TagCache;
import com.lchcommunity.community.dto.QuestionDTO;
import com.lchcommunity.community.mapper.UserMapper;
import com.lchcommunity.community.model.Question;
import com.lchcommunity.community.model.User;
import com.lchcommunity.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String publish(@PathVariable("id") Long id,
                          Model model) {
        QuestionDTO question = questionService.getQuestionById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id",id);
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model
    ) {
        //获取到页面中的数据
        title = title.trim();
        description = description.trim();
        tag = tag.trim();
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());
        //判断是否为空
        if (title == null || title.equals("")) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description.equals("")) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if (!StringUtils.isBlank(invalid)) {
            model.addAttribute("error", invalid+" 标签不存在");
            return "publish";
        }
        //判断用户是否登录
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        //将问题的信息写入到Question类中 在写入数据库
        Question question = new Question();
        question.setCreator(user.getId());
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);

        question.setId(id);
        //更新或插入操作
        questionService.updateOrInsert(question);
        return "redirect:/";
    }
}
