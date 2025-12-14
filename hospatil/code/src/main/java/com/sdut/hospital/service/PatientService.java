package com.sdut.hospital.service;

import com.sdut.hospital.po.Patient;

import java.io.IOException;
import java.util.List;



public interface PatientService {
    List<Patient> findPatientsByDoctorId(Long id);
    Patient findById(Long id);
    long count();
    List<Patient> findAll();
    Patient addOne(Patient patient);
    Patient update(Patient patient);
    boolean deleteById(Long id);
     Patient  check(String name, String password);
     Patient  getPatientById(Long id);
     Patient findName(String name);
    public void save(Patient patient);
    boolean updateById(Long id);
    byte[] exportPatients() throws IOException;
}
