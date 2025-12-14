package com.sdut.hospital.mapper;

import com.sdut.hospital.po.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sdut.hospital.po.Examination;

import java.sql.Date;
import java.util.List;


@Repository
public interface ExaminationMapper extends JpaRepository<Examination, Long> {
    List<Examination> findAllByPatient_Id(Long id);

    long count();

    List<Examination> findAllByCreatTime(Date date);

    int countAllByCreatTime(Date date);

    List<Examination> findAllByCreatTimeAndPatient_Id(Date date, Long id);

    @Query(value = "SELECT DISTINCT examination.creat_time FROM examination left join patient on patient.id = examination.patient_id WHERE patient_id = :id", nativeQuery = true)
    List<Date> findDistinctDate(@Param("id") Long patiebtid);

    Examination findByContent(String content);
    @Query(value = "SELECT DISTINCT DATE_FORMAT(creat_time, '%Y%m%d') AS creat_time FROM examination  ORDER BY creat_time" ,nativeQuery = true)
    List<String> findexam_date();
    @Query(value = "select count(*) from examination GROUP BY creat_time ; ",nativeQuery = true)
    List<Integer> findexam_num();
    @Query(value = "select count(distinct content) from examination",nativeQuery = true)
    Long count_exam();
}
