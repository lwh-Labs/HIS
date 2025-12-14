package com.sdut.hospital.mapper;

import com.sdut.hospital.po.Doctor;
import com.sdut.hospital.po.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleMapper extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDoctor(Doctor doctor);

    List<Schedule> findByStartDayBetween(String startDay, String endDay);

    List<Schedule> findByDoctorAndStartDayBetween(Doctor doctor, String startDay, String endDay);
}
