package com.sdut.hospital.controller;

import com.sdut.hospital.po.Administrator;
import com.sdut.hospital.po.Doctor;
import com.sdut.hospital.po.Patient;
import com.sdut.hospital.service.AdministratorService;
import com.sdut.hospital.service.DoctorService;
import com.sdut.hospital.service.PatientService;
import com.sdut.hospital.Util.PwdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {//处理用户注册请求
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private PatientService patientService;

    @GetMapping("/register")
    public String registerPage() {
        return "register.html";
    }

    @PostMapping("/register")
    public String register(String name, String password, String phone, String captcha, String usertype, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 验证验证码
        String sessionCaptcha = (String) request.getSession().getAttribute("verifyCode");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            redirectAttributes.addFlashAttribute("message", "验证码错误");
            return "redirect:/register";
        }

        if (usertype == null) {
            redirectAttributes.addFlashAttribute("message", "注册异常");
            return "redirect:/register";
        }

        // 验证手机号长度
        if (phone == null || phone.length() != 11) {
            redirectAttributes.addFlashAttribute("message", "手机号必须为11位");
            return "redirect:/register";
        }

        try {// 根据用户类型选择注册的对象
            if (usertype.equals("admin")) {
                Administrator administrator = new Administrator();
                administrator.setName(name);
                administrator.setPassword(PwdUtil.md5(password)); // 使用MD5加密密码
                administrator.setPhone(phone);
                administratorService.save(administrator);
            } else if (usertype.equals("doctor")) {
                Doctor doctor = new Doctor();
                doctor.setName(name);
                doctor.setPassword(PwdUtil.md5(password)); // 使用MD5加密密码
                doctor.setPhone(phone);
                doctorService.save(doctor);
            } else if (usertype.equals("patient")) {
                Patient patient = new Patient();
                patient.setName(name);
                patient.setPassword(PwdUtil.md5(password)); // 使用MD5加密密码
                patient.setPhone(phone);
                patientService.save(patient);
            }
            redirectAttributes.addFlashAttribute("successMessage", "注册成功！");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "注册失败");
            return "redirect:/register";
        }
    }
}
