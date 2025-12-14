package com.sdut.hospital.service;

import com.sdut.hospital.po.Doctor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sdut.hospital.mapper.ExaminationMapper;
import com.sdut.hospital.po.Examination;
import com.sdut.hospital.po.Patient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;


@Service
public class ExaminationServiceImpl implements ExaminationService{
    @Autowired
    ExaminationMapper examinationMapper;
    @Override
    public Examination findByContent(String content) {
        return examinationMapper.findByContent(content);
    }

    @Override
    public List<String> findexam_date() {
        return examinationMapper.findexam_date();
    }

    @Override
    public List<Integer> findexam_num() {
        return examinationMapper.findexam_num();
    }

    @Override
    public Long count_exam() {
        return examinationMapper.count_exam();
    }

    @Override
    public List<Examination> findAllByPatient_Id(Long id) {
        return examinationMapper.findAllByPatient_Id(id);
    }

    @Transactional
    @Override
    public Examination save(Examination examination, Patient patient) {
        examination.setPatient(patient);
        return examinationMapper.save(examination);
    }

    @Override
    public List<Examination> findAllByDate(Date date) {
        return examinationMapper.findAllByCreatTime(date);
    }

    @Override
    public int countAllByDate(Date date) {
        return examinationMapper.countAllByCreatTime(date);
    }

    @Override
    public List<Examination> findAll() {
        return examinationMapper.findAll();
    }

    @Override
    public List<Date> findDistinctDate(Long id) {
        return examinationMapper.findDistinctDate(id);
    }


    @Override
    public Examination addOne(Examination examination) {

        return examinationMapper.save(examination);
    }

    @Override
    public List<Examination> findAllByDateAndPatient_Id(Date date, Long id) {
        return examinationMapper.findAllByCreatTimeAndPatient_Id(date,id);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try {
            examinationMapper.deleteById(id);
        }catch (RuntimeException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public Examination update(Examination examination) {
        return examinationMapper.save(examination);
    }

    @Override
    public Examination findById(Long id) {
        return examinationMapper.findById(id).get();
    }

    @Override
    public byte[] exportExams() throws IOException {
        Workbook workbook = new XSSFWorkbook(); // 使用XSSFWorkbook创建Excel工作簿
        Sheet sheet = workbook.createSheet("Exams");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Check_time");
        headerRow.createCell(2).setCellValue("Content");
        headerRow.createCell(3).setCellValue("Creat_time");
        headerRow.createCell(4).setCellValue("Isfinished");
        headerRow.createCell(5).setCellValue("Price");
        headerRow.createCell(6).setCellValue("Patient_id");

        // 获取数据并填充到Excel表格中
        List<Examination> exams = examinationMapper.findAll();
        int rowNum = 1;
        for (Examination exam : exams) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(exam.getId()); // 确保ID是整数或长整型
            row.createCell(1).setCellValue(exam.getCheckTime());
            row.createCell(2).setCellValue(exam.getContent());
            row.createCell(3).setCellValue(exam.getCreatTime());
            row.createCell(4).setCellValue(exam.getIsfinished());
            row.createCell(5).setCellValue(exam.getPrice());
            row.createCell(6).setCellValue(exam.getPatient().getId());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }

}
