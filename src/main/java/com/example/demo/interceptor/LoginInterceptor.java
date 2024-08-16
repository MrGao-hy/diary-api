package com.example.demo.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.system.mapper.UsersMapper;
import com.example.demo.utils.BeanUtil;
import com.example.demo.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.system.entity.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
        String uri = request.getRequestURI();
        String token = request.getHeader("Authorization");

        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8080");

        // 判断当前请求地址是否需要拦截
        if (token == null) {
            authFailOutput(response, "没有登录，请登录后在操作");
            return false;
        }

        try {
            // 验证token
            DecodedJWT verify = JWTUtils.verify(token);
            // 获取id存储起来
            String userId = verify.getClaim("id").asString();
            final UsersMapper usersMapper = BeanUtil.getBean(UsersMapper.class);
            final HostHolder hostHolder = BeanUtil.getBean(HostHolder.class);
            Users user = usersMapper.selectById(userId);
            if (user == null) {
                authFailOutput(response, "用户不存在,或者已经注销了", 40001);
                return false;
            }
            hostHolder.setUser(user);

            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            authFailOutput(response, "无效签名！");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            authFailOutput(response, "token过期！");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            authFailOutput(response, "token算法不一致！");
        } catch (Exception e) {
            e.printStackTrace();
            authFailOutput(response, "token无效！");
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        final HostHolder hostHolder = BeanUtil.getBean(HostHolder.class);
        // 接口调用完清除掉，防止内存滥用
        hostHolder.clear();
    }

    /**
     * json输出
     *
     * @param response
     * @throws IOException
     */
    private void authFailOutput(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.write(objectMapper.writeValueAsString(Result.fail(40001, msg)));
        out.flush();
    }

    private void authFailOutput(HttpServletResponse response, String msg, Integer code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.write(objectMapper.writeValueAsString(Result.fail(code, msg)));
        out.flush();
    }
}
