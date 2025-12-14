package com.sdut.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;



@Controller
public class LogoutController {
    //处理注销请求，使当前会话失效并重定向到登录页面。
    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().invalidate();
        return "redirect:/login";
    }
}
