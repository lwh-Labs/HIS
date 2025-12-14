package com.sdut.hospital.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sdut.hospital.po.Drug;

import java.sql.Date;
import java.util.List;



public interface DrugMapper extends JpaRepository<Drug, Long>, JpaSpecificationExecutor<Drug> {
    List<Drug> findAllByPatient_Id(Long id);

    long count();

    List<Drug> findAllByCreatDate(Date date);

    int countAllByCreatDate(Date date);

    List<Drug> findAllByCreatDateAndPatient_Id(Date date, Long id);

    @Query(value = "SELECT DISTINCT drug.creat_date FROM drug left join patient on patient.id = drug.patient_id WHERE patient_id = :id", nativeQuery = true)
    List<Date> findDistinctDate(@Param("id") Long patientid);

    Drug findByDrugname(String drugname);
    @Query(value = "SELECT DISTINCT DATE_FORMAT(`creat_date`, '%Y%m%d') AS `creat_date` FROM drug;\n" +
            "\n",nativeQuery = true)
    List<String> finddrug_date();
    @Query(value = "select count(*) from drug GROUP BY creat_date; ",nativeQuery = true)
    List<Integer> finddrug_num();
    @Query(value = "select count(distinct drugname) from drug",nativeQuery = true)
    Long count_drug();
}