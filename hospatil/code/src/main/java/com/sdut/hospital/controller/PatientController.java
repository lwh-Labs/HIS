package com.sdut.hospital.controller;

import com.sdut.hospital.Util.PwdUtil;
import com.sdut.hospital.po.*;
import com.sdut.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

@Controller
public class PatientController {
    @Autowired
    PatientService patientService;
    @Autowired
    DrugService drugService;
    @Autowired
    BedService bedService;
    @Autowired
    DoctorService doctorService;

    @Autowired
    ExaminationService examinationService;
    @Autowired
    public PatientController(PatientService patientService, DrugService drugService) {
        this.patientService = patientService;
        this.drugService = drugService;
    }

    //显示病人主页，包括当天的药物列表、床位信息和医生信息。
    @GetMapping("/patient/index")
    public String Index(@RequestParam(value = "date", required = false)
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                        Model model, HttpServletRequest httpServletRequest) {
        String name = httpServletRequest.getSession().getAttribute("username").toString();
        Patient patient = patientService.findName(name);
        // 加载病人信息
        patient = patientService.getPatientById(patient.getId());
        model.addAttribute("patient", patient);

        // 加载当天药物列表
        List<Drug> drugs = drugService.findDrugsByPatientAndDate(patient, date);
        model.addAttribute("drugs", drugs);

        // 加载当天床位信息
        Bed bed = bedService.findBedByPatientAndDate(patient, date);
        model.addAttribute("bed", bed);

        // 加载医生信息
        Doctor doctor = doctorService.findDoctorByPatientAndDate(patient, date);
        model.addAttribute("doctor", doctor);

        return "patient_index.html";
    }

    //显示修改病人信息的页面。
    @GetMapping("/patient/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            model.addAttribute("message", "病人信息不存在");
            return "error"; // 返回错误页面或处理错误信息
        }
        model.addAttribute("patient", patient);
        return "patient_modify"; // 返回修改病人的页面
    }

    //处理更新病人信息的请求。
    @PostMapping("/patient/modify")
    public String updatePatient(@RequestParam("id") Long id,
                                @RequestParam("name") String name,
                                @RequestParam("password") String password,
                                @RequestParam("phone") String phone,
                                @RequestParam("addr") String addr,
                                RedirectAttributes redirectAttributes) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            redirectAttributes.addFlashAttribute("message", "病人信息不存在");
            return "redirect:/patient/modify/" + id; // 重定向到修改页面
        }

        patient.setName(name);
        patient.setPhone(phone);
        patient.setAddr(addr);
        if(!password.isEmpty()){
            patient.setPassword(PwdUtil.md5(password)); // 使用MD5加密密码
        }

        if (patientService.update(patient) != null) {
            redirectAttributes.addFlashAttribute("message", "修改成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "修改失败");
        }

        return "redirect:/patient/modify/" + id; // 重定向到修改页面并传递病人ID
    }

    //显示病人账单页面
    @GetMapping("/patient/bill")
    public String patientList(Model model, HttpServletRequest httpServletRequest){
        String name = httpServletRequest.getSession().getAttribute("username").toString();
        Patient patients = patientService.findName(name);
        model.addAttribute("patients",patients);
        return "bill.html";
    }

    //显示病人报告页面，包括检测和药物的日期列表。
    @GetMapping("/patient/patientreport/{id}")
    public String patientReportPage(@PathVariable("id") Long id, Model model, HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getSession().toString());
        String name = httpServletRequest.getSession().getAttribute("username").toString();
        Patient patient = patientService.findName(name);
        List<Date> dates1 = drugService.findDistinctDate(id);
        List<Date> dates2 = examinationService.findDistinctDate(id);
        dates1.addAll(dates2);
        model.addAttribute("dates", dates1);
        model.addAttribute("patient", patient);
        return "patient_report.html";
    }

    //处理生成病人报告的请求。
    @PostMapping("/patient/patientreport")
    public String patientReport(@RequestParam(name = "date", required = false) Date date, Model model, HttpServletRequest httpServletRequest) {
        String name = httpServletRequest.getSession().getAttribute("username").toString();
        Patient patient = patientService.findName(name);

        Double pricedrug = 0.0;
        Double priceExam = 0.0;
        List<Drug> drugs = drugService.findAllByDateAndPatient_Id(date, patient.getId());
        List<Examination> examinations = examinationService.findAllByDateAndPatient_Id(date, patient.getId());

        if (date != null) {
            drugs = drugService.findAllByDateAndPatient_Id(date, patient.getId());
            examinations = examinationService.findAllByDateAndPatient_Id(date, patient.getId());
        }

        if (drugs.isEmpty() && examinations.isEmpty()) {
            // 如果没有数据，则设置 nodata 属性为 true，用于页面显示无数据提示
            model.addAttribute("nodata", true);
        } else {
            if (!drugs.isEmpty()) {
                pricedrug = drugs.stream().collect(Collectors.summingDouble(Drug::getPrice));
                model.addAttribute("drugs", drugs);
                model.addAttribute("pricedrug", String.format("%.2f", pricedrug)); // 保留两位小数
            }
            if (!examinations.isEmpty()) {
                priceExam = examinations.stream().collect(Collectors.summingDouble(Examination::getPrice));
                model.addAttribute("exams", examinations);
                model.addAttribute("priceexam", String.format("%.2f", priceExam)); // 保留两位小数
            }
        }

        model.addAttribute("date", date);
        model.addAttribute("patient", patient);
        return "patientreport.html";
    }

    //显示病人所有报告的页面
    @RequestMapping("/patient/patientreportall")
    public String patientReportAll(Model model, HttpServletRequest httpServletRequest) {
        String name = httpServletRequest.getSession().getAttribute("username").toString();
        Patient patient = patientService.findName(name);

        Double pricedrug = 0.0;
        Double priceExam = 0.0;
        List<Drug> drugs = drugService.findAllByPatient_Id(patient.getId());
        List<Examination> examinations = examinationService.findAllByPatient_Id(patient.getId());

        if (drugs.isEmpty() && examinations.isEmpty()) {
            // 如果没有数据，则设置 nodata 属性为 true，用于页面显示无数据提示
            model.addAttribute("nodata", true);
        } else {
            if (!drugs.isEmpty()) {
                pricedrug = drugs.stream().collect(Collectors.summingDouble(Drug::getPrice));
                model.addAttribute("drugs", drugs);
                model.addAttribute("pricedrug", String.format("%.2f", pricedrug)); // 保留两位小数
            }
            if (!examinations.isEmpty()) {
                priceExam = examinations.stream().collect(Collectors.summingDouble(Examination::getPrice));
                model.addAttribute("exams", examinations);
                model.addAttribute("priceexam", String.format("%.2f", priceExam)); // 保留两位小数
            }
        }

        model.addAttribute("patient", patient);
        return "patientreport.html";
    }

}
