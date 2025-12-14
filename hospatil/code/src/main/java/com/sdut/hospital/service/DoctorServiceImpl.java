package com.sdut.hospital.service;

import com.sdut.hospital.Util.DateUtil;
import com.sdut.hospital.Util.PwdUtil;
import com.sdut.hospital.mapper.DoctorMapper;
import com.sdut.hospital.po.Patient;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sdut.hospital.po.Doctor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;


@Service
public class DoctorServiceImpl implements DoctorService{
    @Autowired
    DoctorMapper doctorMapper;

    @Transactional
    @Override
    public Doctor check(String name, String password) {
        return doctorMapper.findByNameAndPassword(name, password);
    }

    @Override
    public Doctor findByName(String name) {
        return doctorMapper.findByName(name);
    }
    @Override
    public long count(){
        return doctorMapper.count();
    }

    @Override
    public List<Doctor> findAll() {
        return doctorMapper.findAll();
    }

    @Transactional
    @Override
    public Doctor addOne(Doctor doctor) {
        if(!doctor.getPassword().isEmpty()){
            doctor.setPassword(PwdUtil.md5(doctor.getPassword()));
        }
        return doctorMapper.save(doctor);
    }

    @Override
    public Doctor findById(Long id) {
        Optional<Doctor> optionalDoctor = doctorMapper.findById(id);
        return optionalDoctor.orElse(null);
    }
    // 假设这里是从数据库或其他数据源获取医生信息的逻辑
    public List<Doctor> getAllDoctors() {
        return doctorMapper.findAll();
    }

    @Transactional
    @Override
    public Doctor update(Doctor doctor) {
        doctor.setUpdateDate(DateUtil.getTodayDate());
        return doctorMapper.save(doctor);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try {
            doctorMapper.deleteById(id);
        }catch (RuntimeException e){
            System.out.println("删除id:" + id + "失败");
            return false;
        }
        return true;
    }
    public  Doctor findDoctorByPatientAndDate(Patient patient, Date date)
    {
        return null;
    }
    public void save(Doctor doctor) {
        doctorMapper.save(doctor);
    }

    @Override
    public byte[] exportDoctors() throws IOException {
        Workbook workbook = new XSSFWorkbook(); // 使用XSSFWorkbook创建Excel工作簿
        Sheet sheet = workbook.createSheet("Doctors");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Address");
        headerRow.createCell(2).setCellValue("Avatar");
        headerRow.createCell(3).setCellValue("Creat_date");
        headerRow.createCell(4).setCellValue("Name");
        headerRow.createCell(5).setCellValue("Password");
        headerRow.createCell(6).setCellValue("Phone");
        headerRow.createCell(7).setCellValue("Update_date");

        // 获取数据并填充到Excel表格中
        List<Doctor> doctors = doctorMapper.findAll();
        int rowNum = 1;
        for (Doctor doctor : doctors) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(doctor.getId());
            row.createCell(1).setCellValue(doctor.getAddr());
            row.createCell(2).setCellValue(doctor.getAvatar());
            row.createCell(3).setCellValue(doctor.getCreatDate());
            row.createCell(4).setCellValue(doctor.getName());
            row.createCell(5).setCellValue(doctor.getPassword());
            row.createCell(6).setCellValue(doctor.getPhone());
            row.createCell(7).setCellValue(doctor.getUpdateDate());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }

}
