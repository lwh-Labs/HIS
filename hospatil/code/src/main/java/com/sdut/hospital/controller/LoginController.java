package com.sdut.hospital.controller;

import com.sdut.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sdut.hospital.po.Administrator;
import com.sdut.hospital.po.Doctor;
import com.sdut.hospital.po.Patient;
import com.sdut.hospital.service.AdministratorService;
import com.sdut.hospital.service.DoctorService;
import com.sdut.hospital.Util.PwdUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private PatientService patientService;

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    //处理登录请求
    @PostMapping("/login")
    public String login(String username, String password, String captcha, String usertype, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String sessionCaptcha = (String) request.getSession().getAttribute("verifyCode"); // 使用verifyCode键获取验证码
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            redirectAttributes.addFlashAttribute("message", "验证码错误");
            return "redirect:/login";
        }

        if (usertype == null) {
            redirectAttributes.addFlashAttribute("message", "登录异常");
            return "redirect:/login";
        } else if (usertype.equals("admin")) {
            Administrator administrator = administratorService.check(username, PwdUtil.md5(password)); // 使用MD5加密密码
            if (administrator != null) {
                request.getSession().setAttribute("username", administrator.getName());
                request.getSession().setAttribute("usertype", "admin");
                request.getSession().setAttribute("userid", administrator.getId());
                return "redirect:/admin/index";
            }
            redirectAttributes.addFlashAttribute("message", "登录失败");
            return "redirect:/login";
        } else if (usertype.equals("doctor")) {
            Doctor doctor = doctorService.check(username, PwdUtil.md5(password)); // 使用MD5加密密码
            if (doctor != null) {
                if (!doctor.isActive()) {
                    // 如果被禁用，显示错误消息并重定向到登录页面
                    redirectAttributes.addFlashAttribute("message", "您的账户已被禁用，请联系管理员");
                    return "redirect:/login";
                }
                request.getSession().setAttribute("username", doctor.getName());
                request.getSession().setAttribute("usertype", "doctor");
                request.getSession().setAttribute("userid", doctor.getId());
                return "redirect:/doctor/index";
            }
            redirectAttributes.addFlashAttribute("message", "登录失败");
            return "redirect:/login";
        } else if (usertype.equals("patient")) {
            Patient patient = patientService.check(username, PwdUtil.md5(password)); // 使用MD5加密密码
            if (patient != null) {
                if (!patient.isActive()) {
                    // 如果被禁用，显示错误消息并重定向到登录页面
                    redirectAttributes.addFlashAttribute("message", "您的账户已被禁用，请联系管理员");
                    return "redirect:/login";
                }
                request.getSession().setAttribute("username", patient.getName());
                request.getSession().setAttribute("usertype", "patient");
                request.getSession().setAttribute("userid", patient.getId());
                return "redirect:/patient/index";
            }
            redirectAttributes.addFlashAttribute("message", "登录失败");
        }
        redirectAttributes.addFlashAttribute("message", "登录失败");
        return "redirect:/login";
    }

    //处理注销请求
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("username");
        return "redirect:/login";
    }
}
