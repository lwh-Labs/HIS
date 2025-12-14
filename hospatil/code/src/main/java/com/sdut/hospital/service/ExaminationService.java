package com.sdut.hospital.service;

import com.sdut.hospital.po.Drug;
import com.sdut.hospital.po.Examination;
import com.sdut.hospital.po.Patient;

import java.io.IOException;
import java.sql.Date;
import java.util.List;



public interface ExaminationService {
    List<Examination> findAllByPatient_Id(Long id);
    Examination save(Examination examination, Patient patient);
    List<Examination> findAllByDate(Date date);
    int countAllByDate(Date date);
    List<Examination> findAll();
    Examination findById(Long id);
    Examination update(Examination examination);
    boolean deleteById(Long id);
    List<Examination> findAllByDateAndPatient_Id(Date date,Long id);
    List<Date> findDistinctDate(Long id);
    Examination addOne(Examination examination);
    byte[] exportExams() throws IOException, IOException;
    Examination findByContent(String content);

    List<String> findexam_date();

    List<Integer> findexam_num();

    Long count_exam();
}
