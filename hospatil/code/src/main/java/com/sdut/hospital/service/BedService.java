package com.sdut.hospital.service;

import com.sdut.hospital.po.Bed;
import com.sdut.hospital.po.Drug;
import com.sdut.hospital.po.Patient;

import java.io.IOException;
import java.sql.Date;
import java.util.List;



public interface BedService {
    Bed findById(Long id);

    List<Bed> findAll();

    List<Bed> findAllUnLive();

    long count();

    Bed addOne(Bed bed);

    Bed update(Bed bed);

    boolean deleteById(Long id);

    // 添加方法以便查找患者和日期
    Bed findBedByPatientAndDate(Patient patient, Date date);

    byte[] exportBeds() throws IOException, IOException;

    List<Bed> findBedsByPatientName(String patientName);

    Bed findByNumber(Integer number);
}
