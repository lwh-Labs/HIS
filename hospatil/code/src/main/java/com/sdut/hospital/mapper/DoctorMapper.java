package com.sdut.hospital.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sdut.hospital.po.Doctor;

import java.sql.Date;
import java.util.List;


@Repository
public interface DoctorMapper extends JpaRepository<Doctor, Long> {

    Doctor findByNameAndPassword(String name, String password);

    Doctor findByName(String name);


    long count();


    List<Doctor> findAll();
}
