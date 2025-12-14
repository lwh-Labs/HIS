package com.sdut.hospital.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;


@Controller
public class RouterController {//根据用户类型进行路由重定向
    @RequestMapping("/")
    public String router(HttpServletRequest httpServletRequest){
        if(httpServletRequest.getSession().getAttribute("usertype")!=null&&httpServletRequest.getSession().getAttribute("usertype").equals("admin")){
            return "redirect:/admin/index";
        }
        else if(httpServletRequest.getSession().getAttribute("usertype")!=null&&httpServletRequest.getSession().getAttribute("usertype").equals("doctor")){
            return "redirect:/doctor/index";
        }
        return "redirect:/login";
    }
}
