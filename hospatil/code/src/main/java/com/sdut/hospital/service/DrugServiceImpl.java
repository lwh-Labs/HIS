package com.sdut.hospital.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sdut.hospital.mapper.DrugMapper;
import com.sdut.hospital.po.Drug;
import com.sdut.hospital.po.Patient;

import javax.persistence.criteria.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;


@Service
public class DrugServiceImpl implements DrugService{
    @Autowired
    DrugMapper drugMapper;

    @Override
    public List<Drug> findAll() {
        return drugMapper.findAll();
    }

    @Override
    public Drug findById(Long id) {
        return drugMapper.findById(id).get();
    }

    @Override
    public List<Date> findDistinctDate(Long id) {
        return drugMapper.findDistinctDate(id);
    }

    @Override
    public List<Drug> findAllByDateAndPatient_Id(Date date, Long id) {
        return drugMapper.findAllByCreatDateAndPatient_Id(date,id);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try{
            drugMapper.deleteById(id);
        }catch (RuntimeException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public Drug update(Drug drug) {
        return drugMapper.save(drug);
    }

    @Transactional
    @Override
    public Drug save(Drug drug, Patient patient) {
        drug.setPatient(patient);
        return drugMapper.save(drug);
    }

    @Override
    public List<Drug> findAllByPatient_Id(Long id) {
        return drugMapper.findAllByPatient_Id(id);
    }
    @Override
    public List<Drug> findAllByDate(Date date){
        return drugMapper.findAll(new Specification<Drug>(){

            @Override
            public Predicate toPredicate(Root<Drug> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<Date> tdate = root.get("creatDate");
                Predicate predicate = criteriaBuilder.equal(tdate,date);
                return predicate;
            }
        });
    }
    @Override
    public int countAllByDate(Date date){
        return drugMapper.countAllByCreatDate(date);
    }
    // 添加方法以便查找患者和日期
    public List<Drug> findDrugsByPatientAndDate(Patient patient, Date date) {
        // 在这里实现根据患者和日期查找药物信息的逻辑
        // 示例代码，实际实现应该根据数据库或其他存储逻辑查询
        return null;
    }

    @Override
    public Drug addOne(Drug drug) {

        return drugMapper.save(drug);
    }

    @Override
    public byte[] exportDrugs() throws IOException {
        Workbook workbook = new XSSFWorkbook(); // 使用XSSFWorkbook创建Excel工作簿
        Sheet sheet = workbook.createSheet("Drugs");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Creat_date");
        headerRow.createCell(2).setCellValue("Drugname");
        headerRow.createCell(3).setCellValue("Isfinished");
        headerRow.createCell(4).setCellValue("Number");
        headerRow.createCell(5).setCellValue("Price");
        headerRow.createCell(6).setCellValue("Patient_id");

        // 获取数据并填充到Excel表格中
        List<Drug> drugs = drugMapper.findAll();
        int rowNum = 1;
        for (Drug drug : drugs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(drug.getId());
            row.createCell(1).setCellValue(drug.getCreatDate() != null ? drug.getCreatDate().toString() : "");
            row.createCell(2).setCellValue(drug.getDrugname());
            row.createCell(3).setCellValue(drug.getIsfinished());
            row.createCell(4).setCellValue(drug.getNumber());
            row.createCell(5).setCellValue(drug.getPrice());
            row.createCell(6).setCellValue(drug.getPatient().getId());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }
    @Override
    public Drug findByDrugname(String drugname) {
        return drugMapper.findByDrugname(drugname); // 这里需要在 DrugRepository 中定义对应的方法
    }

    @Override
    public List<String> finddrug_date() {
        return drugMapper.finddrug_date();
    }

    @Override
    public List<Integer> finddrug_num() {
        return drugMapper.finddrug_num();
    }

    @Override
    public Long count_drug() {
        return drugMapper.count_drug();
    }
}
