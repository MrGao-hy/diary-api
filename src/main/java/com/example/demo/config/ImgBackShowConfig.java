package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author gaoxianhua
 */
@Configuration
public class ImgBackShowConfig implements WebMvcConfigurer {

    // 获取本地图片
    @Value("${file.local.normal}")
    private String normalLocalPath;
    @Value("${file.local.admin}")
    private String adminLocalPath;
    @Value("${file.local.image}")
    private String imageLocalPath;
    @Value("${file.local.video}")
    private String videoLocalPath;

    /**
     * <a href="http://127.0.0.1:8080/avatar/1702640817956.jpg">访问本地图片路径</a> 就可以访问本地图片路径了
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler(相对路径)
        //addResourceLocations(绝对路径)
        //System.getProperty("user.dir") 获取当前项目的绝对路径
        //
        registry.addResourceHandler("image/**").addResourceLocations("file:" + imageLocalPath);
        registry.addResourceHandler("video/**").addResourceLocations("file:" + videoLocalPath);
        registry.addResourceHandler("image/normal/**").addResourceLocations("file:" + normalLocalPath);
        registry.addResourceHandler("image/admin/**").addResourceLocations("file:" + adminLocalPath);
    }
}
