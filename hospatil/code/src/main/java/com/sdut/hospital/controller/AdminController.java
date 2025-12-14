package com.sdut.hospital.controller;
import com.sdut.hospital.Util.DateUtil;
import com.sdut.hospital.Util.PwdUtil;
import com.sdut.hospital.mapper.DoctorMapper;
import com.sdut.hospital.po.*;
import com.sdut.hospital.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class AdminController {
    @Autowired
    PatientService patientService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    DrugService drugService;
    @Autowired
    ExaminationService examinationService;
    @Autowired
    BedService bedService;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    ScheduleService scheduleService;
    SimpleDateFormat simpledateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //显示所有排班信息页面
    @GetMapping("admin/schedule")
    public String list(Model model) {
        List<Schedule> schedules = scheduleService.findAll();
        model.addAttribute("schedules", schedules);
        return "admin_schedule";
    }
    //根据医生姓名搜索排班信息页面
    @GetMapping("/admin/schedule/search")
    public String search(@RequestParam("name") String name, Model model) {
        List<Schedule> schedules = scheduleService.findByName(name);
        model.addAttribute("schedules", schedules);
        return "admin_schedule";
    }

    //删除特定的排班信息
    @GetMapping("/admin/schedule/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        scheduleService.deleteById(id);
        return "redirect:/admin/schedule"; // 重定向到列表页面
    }

    //编辑排班信息页面
    @GetMapping("/admin/schedule/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Schedule schedule = scheduleService.findById(id);
        model.addAttribute("schedule", schedule);
        // 加载星期数组，用于填充下拉框选项
        model.addAttribute("weekDays", Arrays.asList("星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"));
        return "admin_schedule_edit"; // 确保有相应的编辑模板
    }

    //保存编辑后的排班信息
    @PostMapping("/admin/schedule/save")
    public String save(@ModelAttribute Schedule schedule, @RequestParam("doctorName") String doctorName) {
        Doctor doctor = doctorService.findByName(doctorName);
        if (doctor != null) {
            schedule.setDoctorId(doctor.getId());
            schedule.setDoctorName(doctor.getName());
            scheduleService.save(schedule);
        } else {
            // 处理医生不存在的情况
        }
        return "redirect:/admin/schedule"; // 重定向到列表页面
    }

    //显示添加排班信息页面
    @GetMapping("/admin/scheduleadd")
    public String scheduleAddPage(Model model) {
        List<Doctor> doctors = doctorService.findAll();
        model.addAttribute("doctors", doctors);
        return "admin_schedule_add";
    }

    // 处理添加排班信息请求
    @PostMapping("/admin/scheduleadd")
    public String scheduleAdd(String doctorName, String startDay, String endDay, RedirectAttributes redirectAttributes) {
        Schedule schedule = new Schedule();
        schedule.setStartDay(startDay);
        schedule.setEndDay(endDay);

        // Validate input and retrieve the doctor by name
        if (doctorName == null || doctorName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请输入有效的医生姓名");
            return "redirect:/admin/scheduleadd";
        }

        Doctor doctor = doctorService.findByName(doctorName);
        if (doctor == null) {
            redirectAttributes.addFlashAttribute("message", "未找到医生，请输入有效的医生姓名");
            return "redirect:/admin/scheduleadd";
        }

        // Set the doctor for the schedule
        schedule.setDoctor(doctor);

        // Save the schedule
        Schedule savedSchedule = scheduleService.addOne(schedule);
        if (savedSchedule != null) {
            redirectAttributes.addFlashAttribute("message", "添加成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "添加失败");
        }

        return "redirect:/admin/schedule";
    }

    //显示医生添加页面
    @GetMapping("/admin/doctoradd")
    public String doctorAddPage(){
        return "admin_doctor_add.html";
    }
    // 添加医生页面
    @PostMapping("/admin/doctoradd")
    public String doctorAdd(String name, String password, String phone, String addr, RedirectAttributes redirectAttributes){
        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setPassword(PwdUtil.md5(password)); // 使用MD5加密密码
        doctor.setPhone(phone);
        doctor.setAddr(addr);
        if(doctorService.addOne(doctor) != null){
            redirectAttributes.addFlashAttribute("message","添加成功");
        } else {
            redirectAttributes.addFlashAttribute("message","添加失败");
        }
        return "redirect:/admin/doctor";
    }

    //// 编辑医生页面
    @GetMapping("/admin/doctoredit/{id}")
    public String docotrEditPage(@PathVariable("id") Long id, Model model){
        Doctor doctor = doctorService.findById(id);
        model.addAttribute("doctor",doctor);
        return "admin_doctor_edit.html";
    }

    // 显示管理员首页
    @RequestMapping("/admin/index")
    public String adminIndex(Model model) {
        long patientCount = patientService.count();
        long doctorCount = doctorService.count();
        int newDrugCount = drugService.countAllByDate(DateUtil.getTodayDate());
        int newExamCount = examinationService.countAllByDate(DateUtil.getTodayDate());
        long bedCount = bedService.count();
        Map<String, List<Schedule>> dailySchedules = scheduleService.findDailySchedules();

        // 排序顺序：星期一到星期天
        Map<String, List<Schedule>> sortedDailySchedules = new LinkedHashMap<>();
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
        for (String day : weekDays) {
            if (dailySchedules.containsKey(day)) {
                sortedDailySchedules.put(day, dailySchedules.get(day));
            } else {
                sortedDailySchedules.put(day, new ArrayList<>());
            }
        }
        // 计算被安排排班的医生数量
        Set<Long> scheduledDoctorIds = new HashSet<>();
        for (List<Schedule> schedules : dailySchedules.values()) {
            for (Schedule schedule : schedules) {
                scheduledDoctorIds.add(schedule.getDoctor().getId());
            }
        }
        int scheduleDoctorCount = scheduledDoctorIds.size();

        model.addAttribute("patientCount", patientCount);
        model.addAttribute("doctorCount", doctorCount);
        model.addAttribute("drugCount", newDrugCount);
        model.addAttribute("examCount", newExamCount);
        model.addAttribute("bedCount", bedCount);
        model.addAttribute("dailySchedules", sortedDailySchedules);
        model.addAttribute("scheduleDoctorCount", scheduleDoctorCount);

        return "admin_index";
    }

    // 显示医生列表页面
    @GetMapping("/admin/doctor")
    public String doctorList(Model model){
        List<Doctor> doctors = doctorService.findAll();
        model.addAttribute("imageUrl", "/dist/img/doctor.png");
        model.addAttribute("doctors",doctors);
        return "admin_doctor.html";

    }

    // 启用医生
    @GetMapping("/admin/doctor/enable/{id}")
    public String enableDoctor(@PathVariable("id") Long id) {
        Doctor doctor = doctorService.findById(id);
        doctor.setActive(true); // 设置为启用状态
        doctorService.save(doctor);
        return "redirect:/admin/doctor";
    }

    // 禁用医生
    @GetMapping("/admin/doctor/disable/{id}")
    public String disableDoctor(@PathVariable("id") Long id) {
        Doctor doctor = doctorService.findById(id);
        doctor.setActive(false); // 设置为禁用状态
        doctorService.save(doctor);
        return "redirect:/admin/doctor";
    }

    // 显示患者列表页面
    @GetMapping("/admin/patient")
    public String patientList(Model model){
        List<Patient> patients = patientService.findAll();
        model.addAttribute("imageUrl", "/dist/img/patient.png");
        model.addAttribute("imageUrl", "/dist/img/patient.png");

        model.addAttribute("patients",patients);
        return "admin_patient.html";
    }

    // 显示Echarts页面
    @GetMapping("/admin/echarts")
    public String echartsList(Model model){
        List<String> drug_date1 = drugService.finddrug_date();
        ArrayList<String> drug_date = new ArrayList<>();
        for (String date : drug_date1) {
            drug_date.add(date);
        }
        System.out.println("list："+drug_date);
        List<Integer> drug_num = drugService.finddrug_num();

        List<String> drug_date2 = examinationService.findexam_date();
        List<Integer> drug_num2 = examinationService.findexam_num();

        Long count_doctor = doctorService.count();
        Long count_bed = bedService.count();
        Long count_patient = patientService.count();
        Long count_exam_ = examinationService.count_exam();
        Long count_drug = drugService.count_drug();
        ArrayList<Integer> count_list = new ArrayList<>();
        count_list.add(Math.toIntExact(count_patient));
        count_list.add(Math.toIntExact(count_bed));
        count_list.add(Math.toIntExact(count_doctor));
        count_list.add(Math.toIntExact(count_exam_));
        count_list.add(Math.toIntExact(count_drug));


//        List<Doctor> doctors = doctorService.findAll();
//        model.addAttribute("imageUrl", "/dist/img/doctor.png");

        // 将数据传递到前端页面
        model.addAttribute("drug_date",drug_date);
        model.addAttribute("drug_num",drug_num);
        model.addAttribute("drug_date2",drug_date2);
        model.addAttribute("drug_num2",drug_num2);
        model.addAttribute("count_list",count_list);
        return "admin_echarts.html";
    }


    //显示药品添加页面
    @GetMapping("/admin/drugadd")
    public String drugAddPage(){
        return "admin_drug_add.html";
    }

    //处理药品添加请求
    @PostMapping("/admin/drugadd")
    public String drugAdd(String name, Integer num,Float price, RedirectAttributes redirectAttributes){
        Drug drug = new Drug();
        drug.setDrugname(name);
        drug.setNumber(num);
        drug.setPrice(price);
        if(drugService.addOne(drug)!=null){
            redirectAttributes.addFlashAttribute("message","添加成功");
        }else {
            redirectAttributes.addFlashAttribute("message","添加失败");
        }
        return "redirect:/admin/drug";
    }

    //处理医生编辑请求
    @PostMapping("/admin/doctoredit")
    public String doctorEdit(Long id,String name,String password,String phone,String addr,RedirectAttributes redirectAttributes){
        Doctor doctor = doctorService.findById(id);
        doctor.setName(name);
        doctor.setPhone(phone);
        doctor.setAddr(addr);
        if(!password.isEmpty()){
            doctor.setPassword(PwdUtil.md5(password)); // 使用MD5加密密码
        }
        if(doctorService.update(doctor) != null){
            redirectAttributes.addFlashAttribute("message","修改成功");
        } else {
            redirectAttributes.addFlashAttribute("message","修改失败");
        }
        return "redirect:/admin/doctor";
    }

    //处理医生删除请求
    @GetMapping("/admin/doctordel/{id}")
    public String doctorDel(@PathVariable Long id,RedirectAttributes redirectAttributes){
        List<Patient> patients = patientService.findPatientsByDoctorId(id);
        if (patients.isEmpty()) {
            if (doctorService.deleteById(id)) {
                redirectAttributes.addFlashAttribute("message", "删除成功");
            } else {
                redirectAttributes.addFlashAttribute("message", "删除失败");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "删除失败，医生还有关联的患者");
        }
        return "redirect:/admin/doctor";
    }

    //显示医生详细信息页面
    @GetMapping("/admin/doctordetail/{id}")
    public String doctorDetail(@PathVariable("id") Long id , Model model){
        Doctor doctor = doctorService.findById(id);
        List<Patient> patients = patientService.findPatientsByDoctorId(id);
        model.addAttribute("doctor",doctor);
        model.addAttribute("patients",patients);
        return "admin_doctor_detail.html";
    }

    //显示患者添加页面
    @GetMapping("/admin/patientadd")
    public String patientAddPage(){
        return "admin_patient_add.html";
    }

    //处理患者添加请求
    @PostMapping("/admin/patientadd")
    public String patientAdd(String name, String password, String phone, String addr, RedirectAttributes redirectAttributes){
        Patient patient = new Patient();
        patient.setName(name);
        patient.setPassword(PwdUtil.md5(password)); // 使用MD5加密密码
        patient.setPhone(phone);
        patient.setAddr(addr);
        if(patientService.addOne(patient) != null){
            redirectAttributes.addFlashAttribute("message","添加成功");
        } else {
            redirectAttributes.addFlashAttribute("message","添加失败");
        }
        return "redirect:/admin/patient";
    }

    //显示患者编辑页面
    @GetMapping("/admin/patientedit/{id}")
    public String patientEditPage(@PathVariable("id") Long id,Model model){
        Patient patient = patientService.findById(id);
        List<Drug> drugs = drugService.findAllByPatient_Id(id);
        List<Examination> examinations = examinationService.findAllByPatient_Id(id);
        model.addAttribute("patient", patient);
        model.addAttribute("drugs", drugs);
        model.addAttribute("exams", examinations);
        return "admin_patient_edit.html";
    }

    //处理患者编辑请求
    @PostMapping("/admin/patientedit")
    public String patientEdit(Long id, String name, String phone, String addr, RedirectAttributes redirectAttributes){
        Patient patient = patientService.findById(id);
        patient.setName(name);
        patient.setPhone(phone);
        patient.setAddr(addr);
        if(patientService.update(patient) != null){
            redirectAttributes.addFlashAttribute("message","修改成功");
        } else {
            redirectAttributes.addFlashAttribute("message","修改失败");
        }
        return "redirect:/admin/patient";
    }

    //患者启用
    @GetMapping("/admin/patient/enable/{id}")
    public String enablePatient(@PathVariable("id") Long id) {
        Patient patient = patientService.findById(id);
        patient.setActive(true); // 设置患者为启用状态
        patientService.save(patient);
        return "redirect:/admin/patient";
    }

    //患者禁用
    @GetMapping("/admin/patient/disable/{id}")
    public String disablePatient(@PathVariable("id") Long id) {
        Patient patient = patientService.findById(id);
        patient.setActive(false); // 设置患者为禁用状态
        patientService.save(patient);
        return "redirect:/admin/patient";
    }

    //处理患者删除请求
    @GetMapping("/admin/patientdel/{id}")
    public String patientdelete(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){
        if(patientService.deleteById(id)){
            redirectAttributes.addFlashAttribute("message","删除成功");
        }else {
            redirectAttributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/patient";
    }


    @GetMapping("/admin/patientinout/{id}")
    public String patientupdate(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){
        if(!patientService.updateById(id)){
                redirectAttributes.addFlashAttribute("message","修改成功！");
        }
        return "redirect:/admin/patient";
    }

    //显示患者分配页面
    @GetMapping("/admin/patientset/{id}")
    public String patientSetPage(@PathVariable("id") Long id,Model model){
        List<Doctor> doctors = doctorService.findAll();
        List<Bed> beds = bedService.findAllUnLive();
        Patient patient = patientService.findById(id);
        model.addAttribute("doctors",doctors);
        model.addAttribute("doctor",patient.getDoctor());
        model.addAttribute("patient",patient);
        model.addAttribute("beds",beds);
        model.addAttribute("bed",patient.getBed());
        return "admin_patient_set.html";
    }

    //处理患者分配请求
    @PostMapping("/admin/patientset/{id}")
    public String patientSet(@PathVariable("id") Long id,Long doctorid,Long bedid,RedirectAttributes redirectAttributes){
        Patient patient = patientService.findById(id);
        if(null!=doctorid){
            if(doctorid!=0){
                Doctor doctor = doctorService.findById(doctorid);
                patient.setDoctor(doctor);
            }else {
                patient.setDoctor(null);
            }
        }else if(null!=bedid){
            if(bedid!=0){
                Bed bed = bedService.findById(bedid);
                patient.setBed(bed);
            }else {
                patient.setBed(null);
            }
        }else return "redirect:/admin/patient";
        if(patientService.update(patient)!=null){
            redirectAttributes.addFlashAttribute("message","分配成功");
        }else {
            redirectAttributes.addFlashAttribute("message","分配失败");
        }
        return "redirect:/admin/patient";
    }

    //显示药品管理页面
    @GetMapping("/admin/drug")
    public String drug(Model model){
        List<Drug> drugs = drugService.findAll();
        model.addAttribute("imageUrl", "/dist/img/drug.png");
        model.addAttribute("drugs",drugs);
        return "admin_drug.html";
    }

    //显示检查项目管理页面
    @GetMapping("/admin/exam")
    public String exam(Model model){
        List<Examination> examinations = examinationService.findAll();
        model.addAttribute("imageUrl", "/dist/img/exame.png");
        model.addAttribute("exams",examinations);
        return "admin_exam.html";
    }

    //显示检查项目添加页面
    @GetMapping("admin/examadd")
    public String examAddPage(Model model){
        List<Patient> patients = patientService.findAll();
        model.addAttribute("patients",patients);
        return "admin_exam_add.html";
    }

    //处理检查项目添加请求
    @PostMapping("admin/examadd")
    public String examAdd(String content, float price,Long patientid,RedirectAttributes redirectAttributes){
        Examination examination = new Examination();
        examination.setContent(content);
        examination.setPrice(price);
        Patient patient = patientService.getPatientById(patientid);

        examination.setPatient(patient);
        if(examinationService.addOne(examination)!=null){
            redirectAttributes.addFlashAttribute("message","添加成功");
        }else {
            redirectAttributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/admin/exam";

    }


    //显示床位管理页面
    @GetMapping("/admin/bed")
    public String bed(Model model){
        List<Bed> beds = bedService.findAll();
        model.addAttribute("imageUrl", "/dist/img/bed.png");
        model.addAttribute("beds",beds);
        return "admin_bed.html";
    }

    //显示药物编辑页面
    @GetMapping("/admin/drugedit/{id}")
    public String drugEditPage(@PathVariable("id") Long id,Model model){
        Drug drug = drugService.findById(id);
        model.addAttribute("drug", drug);

        List<Drug> drugs = drugService.findAll();
        model.addAttribute("drugs", drugs);

        return "admin_drug_edit.html";
    }

    //显示药品编辑页面
    @PostMapping("/admin/drugedit")
    public String drugEdit(@RequestParam("id") Long id,
                           @RequestParam("drugname") String drugname,
                           @RequestParam("number") Integer number,
                           @RequestParam("price") Float price,
                           RedirectAttributes redirectAttributes) {

        Drug drug = drugService.findByDrugname(drugname);

        // 检查药物是否存在
        if (drug == null) {
            redirectAttributes.addFlashAttribute("message", "选择的药物不存在！");
            return "redirect:/admin/drugedit/" + id;
        }

        // 检查药物数量是否超过最大数量
        if (number > drug.getNumber()) {
            redirectAttributes.addFlashAttribute("message", "超过药物数量限制！");
            return "redirect:/admin/drugedit/" + id;
        }

        // 更新药物信息
        drug.setNumber(number);
        drug.setPrice(price); // 更新药物价格

        if (drugService.update(drug) != null) {
            redirectAttributes.addFlashAttribute("message", "修改成功！");
        } else {
            redirectAttributes.addFlashAttribute("message", "修改失败！");
        }

        return "redirect:/admin/patient";
    }

    //显示检查项目编辑页面
    @GetMapping("/admin/examedit/{id}")
    public String examEditPage(@PathVariable("id") Long id,Model model){
        Examination examination = examinationService.findById(id);
        model.addAttribute("exam",examination);
        return "admin_exam_edit.html";
    }

    //处理检查项目编辑请求
    @PostMapping("/admin/examedit")
    public String examEdit(Long id, String content, Date checkdate, Float price,RedirectAttributes redirectAttributes){
        Examination examination = examinationService.findById(id);
        examination.setContent(content);
        examination.setCheckTime(checkdate);
        examination.setPrice(price);
        if(examinationService.update(examination)!=null){
            redirectAttributes.addFlashAttribute("message","修改成功");
        }else {
            redirectAttributes.addFlashAttribute("message","修改失败");
        }
        return "redirect:/admin/exam";
    }

    //处理药品删除请求
    @GetMapping("/admin/drugdel/{id}")
    public String drugDel(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){
        if(drugService.deleteById(id)){
            redirectAttributes.addFlashAttribute("message","删除成功");
        }else {
            redirectAttributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/drug";
    }

    //处理检查项目删除请求
    @GetMapping("/admin/examdel/{id}")
    public String StringexamDel(@PathVariable("id") Long id,RedirectAttributes redirectAttributes){
        if(examinationService.deleteById(id)){
            redirectAttributes.addFlashAttribute("message","删除成功");
        }else {
            redirectAttributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/exam";
    }

    // 显示添加床位页面
    @GetMapping("admin/bedadd")
    public String bedAddPage(){
        return "admin_bed_add.html";
    }

    //处理添加床位请求
    @PostMapping("admin/bedadd")
    public String bedAdd(@RequestParam("number") Integer number, RedirectAttributes redirectAttributes) {
        // 检查床号是否已经存在
        Bed existingBed = bedService.findByNumber(number);
        if (existingBed != null) {
            redirectAttributes.addFlashAttribute("message", "床号已存在，请选择其他床号");
            return "redirect:/admin/bedadd";
        }

        // 如果床号不存在，则添加新的床位
        Bed bed = new Bed();
        bed.setNumber(number);
        if (bedService.addOne(bed) != null) {
            redirectAttributes.addFlashAttribute("message", "添加成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "添加失败");
        }
        return "redirect:/admin/bed";
    }


    //显示床位编辑页面
    @GetMapping("/admin/bededit/{id}")
    public String bedEditPage(@PathVariable("id") Long id,Model model){
        Bed bed = bedService.findById(id);
        model.addAttribute("bed",bed);
        return "admin_bed_edit.html";
    }

    //处理床位编辑页面
    @PostMapping("/admin/bededit")
    public String bedEdit(Long id,Integer number,RedirectAttributes redirectAttributes){
        Bed bed = bedService.findById(id);
        bed.setNumber(number);
        if(bedService.update(bed)!=null){
            redirectAttributes.addFlashAttribute("message","修改成功");
        }else {
            redirectAttributes.addFlashAttribute("message","修改失败");
        }
        return "redirect:/admin/bed";
    }

    //处理床位删除请求
    @GetMapping("/admin/beddel/{id}")
    public String bedDel(@PathVariable("id")Long id,RedirectAttributes redirectAttributes){
        if(bedService.deleteById(id)){
            redirectAttributes.addFlashAttribute("message","删除成功");
        }else {
            redirectAttributes.addFlashAttribute("message","删除失败");
        }
        return "redirect:/admin/bed";
    }


    // 显示患者详细信息页面
    @GetMapping("/admin/patientdetail/{id}")
    public  String patientDetail(@PathVariable("id") Long id,Model model){
        // 查找指定ID的患者信息
        Patient patient = patientService.findById(id);
        List<Drug> drugs = drugService.findAllByPatient_Id(id);// 查找指定患者ID关联的药品信息
        List<Examination> examinations = examinationService.findAllByPatient_Id(id);// 查找指定患者ID关联的检查项目信息
        // 将患者信息、药品信息和检查项目信息添加到模型中
        model.addAttribute("patient",patient);
        model.addAttribute("drugs",drugs);
        model.addAttribute("exams",examinations);
        return "admin_patient_detail.html";
    }

    //显示生成患者报告页面
    @GetMapping("/admin/patientreport/{id}")
    public String patientReportPage(@PathVariable("id") Long id, Model model){
        Patient patient = patientService.findById(id);
        List<Date> dates1 = drugService.findDistinctDate(id);
        List<Date> dates2 = examinationService.findDistinctDate(id);
        dates1.addAll(dates2);
        model.addAttribute("dates", dates1);
        model.addAttribute("patient", patient);
        return "admin_patient_report.html";
    }

    //处理生成患者报告请求
    @PostMapping("/admin/patientreport/{id}")
    public String patientReport(@PathVariable("id") Long id, Date date, Model model){
        Patient patient = patientService.findById(id);
        Double pricedrug = 0.0;
        Double priceExam = 0.0;
        List<Drug> drugs = drugService.findAllByDateAndPatient_Id(date, id);
        List<Examination> examinations = examinationService.findAllByDateAndPatient_Id(date, id);
        if(drugs.size() > 0){
            pricedrug = drugs.stream().collect(Collectors.summingDouble(Drug::getPrice));
            model.addAttribute("drugs", drugs);
            model.addAttribute("pricedrug", String.format("%.2f", pricedrug));
        }
        if(examinations.size() > 0){
            priceExam = examinations.stream().collect(Collectors.summingDouble(Examination::getPrice));
            model.addAttribute("exams", examinations);
            model.addAttribute("priceexam", String.format("%.2f", priceExam));
        }
        model.addAttribute("date", date);
        model.addAttribute("patient", patient);
        return "report.html";
    }

    //// 生成包含患者所有药品和检查项目费用的报告
    @RequestMapping("/admin/patientreportall/{id}")
    public String patientReportAll(@PathVariable Long id, Model model){
        Patient patient = patientService.findById(id);
        Double pricedrug = 0.0;
        Double priceExam = 0.0;
        List<Drug> drugs = drugService.findAllByPatient_Id(id);
        List<Examination> examinations = examinationService.findAllByPatient_Id(id);
        if(drugs.size() > 0){
            pricedrug = drugs.stream().collect(Collectors.summingDouble(Drug::getPrice));
            model.addAttribute("drugs", drugs);
            model.addAttribute("pricedrug", String.format("%.2f", pricedrug));
        }
        if(examinations.size() > 0){
            priceExam = examinations.stream().collect(Collectors.summingDouble(Examination::getPrice));
            model.addAttribute("exams", examinations);
            model.addAttribute("priceexam", String.format("%.2f", priceExam));
        }
        model.addAttribute("patient", patient);
        return "report.html";
    }


    //// 导出医生信息到Excel文件
    @GetMapping("/admin/doctor/export")
    public ResponseEntity<byte[]> exportDoctors() throws IOException {
        byte[] excelData = doctorService.exportDoctors();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=doctors.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // 导出患者信息到Excel文件
    @GetMapping("/admin/patient/export")
    public ResponseEntity<byte[]> exportPatients() throws IOException {
        byte[] excelData = patientService.exportPatients();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=patients.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // 导出药品信息到Excel文件
    @GetMapping("/admin/drug/export")
    public ResponseEntity<byte[]> exportDrugs() throws IOException {
        byte[] excelData = drugService.exportDrugs();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=drugs.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // 导出床位信息到Excel文件
    @GetMapping("/admin/bed/export")
    public ResponseEntity<byte[]> exportBeds() throws IOException {
        byte[] excelData = bedService.exportBeds();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=beds.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // 导出检查项目信息到Excel文件
    @GetMapping("/admin/exam/export")
    public ResponseEntity<byte[]> exportExams() throws IOException {
        byte[] excelData = examinationService.exportExams();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=exams.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // 导出检查项目信息到Excel文件
    @GetMapping("/admin/schedule/export")
    public ResponseEntity<byte[]> exportSchedules() throws IOException {
        byte[] excelData = scheduleService.exportSchedules();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=schedules.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    // 根据患者姓名搜索床位信息
    @GetMapping("/admin/bed/search")
    public String searchBeds(@RequestParam("patientName") String patientName, Model model) {
        List<Bed> beds;
        if (patientName == null || patientName.isEmpty()) {
            beds = bedService.findAll();
        } else {
            beds = bedService.findBedsByPatientName(patientName);
        }
        model.addAttribute("imageUrl", "/dist/img/bed.png");
        model.addAttribute("beds", beds);
        return "admin_bed.html";
    }
}
