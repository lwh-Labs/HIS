package com.sdut.hospital.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PatientInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("username")==null||!request.getSession().getAttribute("usertype").equals("admin")){
            response.sendRedirect("/");// 不是病人，重定向到根路径
            return false;
        }
        return true;// 是病人，继续处理请求
    }
}