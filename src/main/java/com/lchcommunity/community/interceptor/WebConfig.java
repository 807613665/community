package com.lchcommunity.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    SessinoInterceptor sessinoInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessinoInterceptor).addPathPatterns("/**");
    }

    //当添加拦截器后，页面无法访问静态资源，如css.js等  添加下代码可以忽略静态资源
    //如果访问路径是addResourceHandler中的filepath 这个路径 那么就 映射到访问本地的addResourceLocations 的参数的这个路径上
    //这样就可以让别人访问服务器的本地文件了，比如本地图片或者本地音乐视频什么的。
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }

    }
}
