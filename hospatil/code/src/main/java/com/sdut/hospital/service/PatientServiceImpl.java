package com.sdut.hospital.service;

import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sdut.hospital.Util.DateUtil;
import com.sdut.hospital.mapper.PatientMapper;
import com.sdut.hospital.po.Patient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
public class PatientServiceImpl implements PatientService{
    @Autowired
    PatientMapper patientMapper;

    @Transactional(readOnly = true)
    public Patient getPatientById(Long id) {
        Patient patient = patientMapper.findById(id).orElse(null);
        if (patient != null) {
            // 强制初始化药物列表
            patient.getDrugs().size(); // 或者使用 Hibernate.initialize(patient.getDrugs());
        }
        return patient;
    }
    @Override
    public List<Patient> findPatientsByDoctorId(Long id) {
        return patientMapper.findAllByDoctor_Id(id);
    }

    @Override
    public Patient findById(Long id) {
        return (Patient) patientMapper.findById(id).get();
    }

    @Override
    public long count() {
        return patientMapper.count();
    }

    @Override
    public List<Patient> findAll() {
        return patientMapper.findAll();
    }

    @Transactional
    @Override
    public Patient addOne(Patient patient) {
        return patientMapper.save(patient);
    }

    @Transactional
    @Override
    public Patient update(Patient patient) {
        patient.setUpdateDate(DateUtil.getTodayDate());
        return patientMapper.save(patient);
    }
    @Override
    public Patient findName(String name) {
        return patientMapper.findByName(name);
    }
    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try {
            patientMapper.deleteById(id);
        }catch (RuntimeException e){
            System.out.println("删除id: " + id + "失败");
            return false;
        }
        return true;
    }
    @Override
    public Patient check(String name, String password){
        return patientMapper.findByNameAndPassword(name, password);
    }

    public void save(Patient patient) {
        patientMapper.save(patient);
    }


    @Override
    public boolean updateById(Long id) {
        patientMapper.updateById(id);
        return false;
    }

    @Override
    public byte[] exportPatients() throws IOException {
        Workbook workbook = new XSSFWorkbook(); // 使用XSSFWorkbook创建Excel工作簿
        Sheet sheet = workbook.createSheet("Patients");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Address");
        headerRow.createCell(2).setCellValue("Avatar");
        headerRow.createCell(3).setCellValue("Creat_date");
        headerRow.createCell(4).setCellValue("Name");
        headerRow.createCell(5).setCellValue("Phone");
        headerRow.createCell(6).setCellValue("Update_date");
        headerRow.createCell(7).setCellValue("Bed_id");
        headerRow.createCell(8).setCellValue("Doctor_id");
        headerRow.createCell(9).setCellValue("Discharged");
        headerRow.createCell(10).setCellValue("Drugsprice");
        headerRow.createCell(11).setCellValue("Password");

        // 获取数据并填充到Excel表格中
        List<Patient> patients = patientMapper.findAll();
        int rowNum = 1;
        for (Patient patient : patients) {
            Row row = sheet.createRow(rowNum++);
            // 处理可能为null的字段
            row.createCell(0).setCellValue(patient.getId());
            row.createCell(1).setCellValue(patient.getAddr() != null ? patient.getAddr() : "");
            row.createCell(2).setCellValue(patient.getAvatar() != null ? patient.getAvatar() : "");
            row.createCell(3).setCellValue(patient.getCreatDate() != null ? patient.getCreatDate().toString() : "");
            row.createCell(4).setCellValue(patient.getName() != null ? patient.getName() : "");
            row.createCell(5).setCellValue(patient.getPhone() != null ? patient.getPhone() : "");
            row.createCell(6).setCellValue(patient.getUpdateDate() != null ? patient.getUpdateDate().toString() : "");
            row.createCell(7).setCellValue(patient.getBed() != null ? patient.getBed().getId().toString() : "未分配");
            row.createCell(8).setCellValue(patient.getDoctor() != null ? patient.getDoctor().getId().toString() : "未分配");
            row.createCell(9).setCellValue(patient.getDischarged());
            row.createCell(10).setCellValue(patient.getDrugsprice() != null ? patient.getDrugsprice() : 0.0);
            row.createCell(11).setCellValue(patient.getPassword() != null ? patient.getPassword() : "");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }


}
