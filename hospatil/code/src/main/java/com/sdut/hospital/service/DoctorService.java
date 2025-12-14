package com.sdut.hospital.service;

import com.sdut.hospital.po.Doctor;
import com.sdut.hospital.po.Patient;

import java.io.IOException;
import java.sql.Date;
import java.util.List;



public interface DoctorService {
    Doctor check(String name,String password);
    Doctor findByName(String name);
    long count();
    List<Doctor> findAll();
    Doctor addOne(Doctor doctor);
    Doctor findById(Long id);
    Doctor update(Doctor doctor);
    boolean deleteById(Long id);
    public  Doctor findDoctorByPatientAndDate(Patient patient, Date date);
    public void save(Doctor doctor);
    public List<Doctor> getAllDoctors();
    byte[] exportDoctors() throws IOException, IOException;
}
