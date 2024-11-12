package com.example.demo.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.example.demo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Web MVC 配置类
 *
 * @author pan_junbiao
 **/
@Configuration
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 控制器配置
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toIndexPage").setViewName("/index");
        registry.addViewController("/").setViewName("/index");
    }

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册Interceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());

        registration.addPathPatterns("/**"); //所有路径都被拦截

        // 白名单
        List<String> patterns = new ArrayList<String>();
        patterns.add("/user/login");
        patterns.add("/user/register");
        patterns.add("/font/**");
        patterns.add("/");
        patterns.add("/request/**");
        patterns.add("/file/diary/**");
        patterns.add("/error");
        registration.excludePathPatterns(patterns); //添加不拦截路径
        registration.order(Ordered.HIGHEST_PRECEDENCE); //设置拦截器的顺序
    }


        @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
        public StandardServletMultipartResolver multipartResolver() {
            StandardServletMultipartResolver multipartResolver = new DoNotCleanupMultipartResolver();
            multipartResolver.setResolveLazily(false);
            return multipartResolver;
        }

        public static class DoNotCleanupMultipartResolver extends StandardServletMultipartResolver {
            @Override
            public void cleanupMultipart(MultipartHttpServletRequest request) {
                try{
                    if(request instanceof StandardMultipartHttpServletRequest){
                        ((StandardMultipartHttpServletRequest) request).getRequest().getParts().clear();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


}
