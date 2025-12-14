package com.sdut.hospital.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//管理员拦截器用于验证用户是否为管理员。
public class AdministratorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("username")==null||!request.getSession().getAttribute("usertype").equals("admin")){
            response.sendRedirect("/");// 如果不是管理员，重定向到根路径
            return false;
        }
        return true;// 是管理员，继续处理请求
    }
}
