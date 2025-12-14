package com.sdut.hospital.controller;

import com.sdut.hospital.service.DrugService;
import com.sdut.hospital.service.ExaminationService;
import com.sdut.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sdut.hospital.po.Doctor;
import com.sdut.hospital.po.Drug;
import com.sdut.hospital.po.Examination;
import com.sdut.hospital.po.Patient;
import com.sdut.hospital.service.DoctorService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@Controller
public class DoctorController {
    @Autowired
    DoctorService doctorService;
    @Autowired
    PatientService patientService;
    @Autowired
    DrugService drugService;
    @Autowired
    ExaminationService examinationService;

    //医生主页，显示医生负责的患者列表。
    @GetMapping("/doctor/index")
    public String doctorpage(Model model, HttpServletRequest httpServletRequest){
        System.out.println(httpServletRequest.getSession().toString());
        String name = httpServletRequest.getSession().getAttribute("username").toString();
        Doctor doctor = doctorService.findByName(name);
        model.addAttribute("imageUrl", "/dist/img/patient.png");
        model.addAttribute("patients",patientService.findPatientsByDoctorId(doctor.getId()));
        return "doctorindex.html";
    }

    //查看特定患者的详细信息，包括用药记录和检查记录。
    @GetMapping("/doctor/patient/{id}")
    public String patient(@PathVariable("id") Long id, Model model){
        Patient patient = patientService.findById(id);
        List<Drug> drugs = drugService.findAllByPatient_Id(patient.getId());
        List<Examination> examinations = examinationService.findAllByPatient_Id(patient.getId());
        model.addAttribute("drugs",drugs);
        model.addAttribute("exams",examinations);
        model.addAttribute("patient",patient);
        return "doctor_patient.html";
    }

    //医生为患者添加药物页面。
    @GetMapping("/doctor/patient/adddrug/{id}")
    public String patientAddDrug(Long id){
        Drug drug =new Drug();
        drug.setDrugname("qoq");
        Patient patient = new Patient();
        patient.setId(1L);
//        drug.setPatient(patient);
        drugService.save(drug,patient);
        return drug.toString();
    }

    //医生添加药物页面
    @GetMapping("/doctor/adddrug/{id}")
    public String drugAddPage(@PathVariable("id") Long id, Model model) {
        Patient patient = patientService.findById(id);
        model.addAttribute("patient", patient);

        List<Drug> drugs = drugService.findAll();
        model.addAttribute("drugs", drugs);

        return "doctor_patient_drug.html";
    }

    //处理医生添加药物
    @PostMapping("/doctor/adddrug")
    public String drugAdd(Long patientid, String drugname, Integer number, Float price, RedirectAttributes redirectAttributes) {
        Patient patient = patientService.findById(patientid);
        Drug drug = drugService.findByDrugname(drugname);

        // 检查药物是否存在
        if (drug == null) {
            redirectAttributes.addFlashAttribute("message", "选择的药物不存在！");
            return "redirect:/doctor/patient/" + patient.getId();
        }

        // 检查药物数量是否足够
        if (drug.getNumber() < number) {
            redirectAttributes.addFlashAttribute("message", "药物数量不足！");
            return "redirect:/doctor/patient/" + patient.getId();
        }

        // 设置药物信息并保存
        drug.setPatient(patient);
        drug.setNumber(number);
        if (drugService.save(drug, patient) != null) {
            redirectAttributes.addFlashAttribute("message", "成功！");
        } else {
            redirectAttributes.addFlashAttribute("message", "失败！");
        }

        return "redirect:/doctor/patient/" + patient.getId();
    }

    //医生添加检查项页面。
    @GetMapping("/doctor/addexam/{id}")
    public String examAddPage(@PathVariable("id") Long id, Model model) {
        Patient patient = patientService.findById(id);
        List<Examination> examinations = examinationService.findAll(); // 假设该方法返回所有可用的检测项目列表
        model.addAttribute("patient", patient);
        model.addAttribute("examinations", examinations);
        return "doctor_patient_exam";
    }

    //处理医生添加检查
    @PostMapping("/doctor/addexam")
    public String examAdd(Long patientid, String content, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date checkdate, Float price, RedirectAttributes redirectAttributes) {
        Patient patient = patientService.findById(patientid);
        Examination examination = examinationService.findByContent(content);

        if (examination == null) {
            redirectAttributes.addFlashAttribute("message", "选择的检测项目不存在！");
            return "redirect:/doctor/patient/" + patient.getId();
        }

        examination.setPatient(patient);
        examination.setPrice(price);

        if (examinationService.save(examination, patient) != null) {
            redirectAttributes.addFlashAttribute("message", "添加成功！");
        } else {
            redirectAttributes.addFlashAttribute("message", "添加失败！");
        }

        return "redirect:/doctor/patient/" + patient.getId();
    }


    //医生更新患者信息
    @GetMapping("/doctor/patientinout/{id}")
    public String patientupdate(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){
        if(patientService.updateById(id)){
            redirectAttributes.addFlashAttribute("message","删除成功");
        }else {
            redirectAttributes.addFlashAttribute("message","删除失败");
        }

        return "redirect:/doctor/index";
    }



}
