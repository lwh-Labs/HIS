package com.sdut.hospital.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.sdut.hospital.po.Bed;
import com.sdut.hospital.po.Patient;

import java.util.List;

@Repository
public interface BedMapper extends JpaRepository<Bed, Long> {

    long count();

    List<Bed> findAllByPatient(Patient patient);

    @Query(value = "SELECT tempp.id,tempp.number FROM (SELECT bed.*,patient.bed_id AS temp FROM bed LEFT JOIN patient on bed.id=patient.bed_id) AS tempp WHERE tempp.temp is null", nativeQuery = true)
    List<Bed> findUnLive();

    List<Bed> findAllByPatientNameContaining(String patientName);
    Bed findByNumber(Integer number);
}
