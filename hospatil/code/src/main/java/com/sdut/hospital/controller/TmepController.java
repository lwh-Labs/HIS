package com.sdut.hospital.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;


@Controller
public class TmepController {//临时控制器用于测试会话中的用户名信息。
    @RequestMapping("/temp")
    public String hello(HttpSession session){
        System.out.println(session.getAttribute("username"));
        return "temp.html";
    }
}
