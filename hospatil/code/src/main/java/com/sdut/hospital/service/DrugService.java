package com.sdut.hospital.service;

import com.sdut.hospital.po.Drug;
import com.sdut.hospital.po.Patient;
import com.sdut.hospital.po.Doctor;

import java.io.IOException;
import java.sql.Date;
import java.util.List;



public interface DrugService {
    Drug save(Drug drug, Patient patient);
    List<Drug> findAllByPatient_Id(Long Id);
    List<Drug> findAllByDate(Date date);
    int countAllByDate(Date date);
    List<Drug> findAll();
    Drug findById(Long id);
    Drug update(Drug drug);
    boolean deleteById(Long id);
    List<Drug> findAllByDateAndPatient_Id(Date date,Long id);
    List<Date> findDistinctDate(Long id);
    // 添加方法以便查找患者和日期
    public List<Drug> findDrugsByPatientAndDate(Patient patient, Date date);
    Drug addOne(Drug drug);
    byte[] exportDrugs() throws IOException, IOException;
    Drug findByDrugname(String drugname);

    List<String> finddrug_date();

    List<Integer> finddrug_num();

    Long count_drug();
}
