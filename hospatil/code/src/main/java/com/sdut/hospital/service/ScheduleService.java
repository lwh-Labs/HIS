package com.sdut.hospital.service;

import com.sdut.hospital.po.Doctor;
import com.sdut.hospital.po.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    public List<Schedule> findAll();
    void save(Schedule doctorSchedule);
    public List<Schedule> findByName(String name);
    public Schedule findById(Long id );
    public List<Schedule> findByDoctor(Doctor doctor);
    public void deleteById(Long id);
    public Schedule addOne(Schedule schedule);
    @Mapper
    public Map<String, List<Schedule>> findDailySchedules();
    byte[] exportSchedules() throws IOException;

}
