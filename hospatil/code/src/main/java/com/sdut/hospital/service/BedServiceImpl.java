package com.sdut.hospital.service;

import com.sdut.hospital.mapper.BedMapper;
import com.sdut.hospital.po.Patient;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sdut.hospital.po.Bed;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;


@Service
public class BedServiceImpl implements BedService {
    @Autowired
    BedMapper bedMapper;

    @Override
    public List<Bed> findAllUnLive() {
        return bedMapper.findUnLive();
    }

    @Override
    public long count() {
        return bedMapper.count();
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try {
            bedMapper.deleteById(id);
        } catch (RuntimeException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public Bed addOne(Bed bed) {
        return bedMapper.save(bed);
    }

    @Transactional
    @Override
    public Bed update(Bed bed) {
        return bedMapper.save(bed);
    }

    @Override
    public List<Bed> findAll() {
        return bedMapper.findAll();
    }

    @Override
    public Bed findById(Long id) {
        return bedMapper.findById(id).get();
    }

    // 实现根据患者和日期查找床位信息的方法
    public Bed findBedByPatientAndDate(Patient patient, Date date) {
        return null;
    }

    @Override
    public byte[] exportBeds() throws IOException {
        Workbook workbook = new XSSFWorkbook(); // 使用XSSFWorkbook创建Excel工作簿
        Sheet sheet = workbook.createSheet("Beds");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Number");

        // 获取数据并填充到Excel表格中
        List<Bed> beds = bedMapper.findAll();
        int rowNum = 1;
        for (Bed bed : beds) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bed.getId());
            row.createCell(1).setCellValue(bed.getNumber());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }

    public List<Bed> findBedsByPatientName(String patientName) {
        return bedMapper.findAllByPatientNameContaining(patientName);
    }

    @Override
    public Bed findByNumber(Integer number) {
        return null;
    }


}
