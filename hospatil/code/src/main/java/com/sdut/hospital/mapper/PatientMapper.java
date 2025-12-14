package com.sdut.hospital.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.sdut.hospital.po.Patient;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



public interface PatientMapper extends JpaRepository<Patient, Long> {
    public Patient findByNameAndPassword(String name, String password);

    Patient findByName(String name);

    long count();

    List<Patient> findAll();

    List<Patient> findAllByDoctor_Id(Long id);

    @Query(value = "SELECT * FROM patient WHERE id = ?1", nativeQuery = true)
    Patient getPatientById(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE patient SET discharged = CASE WHEN discharged = 1 THEN 0 ELSE 1 END WHERE id = ?1", nativeQuery = true)
    void updateById(Long id);

    List<Patient> findByIdIn(List<Long> selectedIds);

}
