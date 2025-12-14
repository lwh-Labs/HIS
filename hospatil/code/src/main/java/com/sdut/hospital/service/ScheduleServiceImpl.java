package com.sdut.hospital.service;

import com.sdut.hospital.mapper.ScheduleMapper;
import com.sdut.hospital.po.Doctor;
import com.sdut.hospital.po.Schedule;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> findAll() {
        return scheduleMapper.findAll();
    }

    @Override
    public List<Schedule> findByDoctor(Doctor doctor) {
        return scheduleMapper.findByDoctor(doctor);
    }

    @Override
    public Schedule findById(Long id) {
        Optional<Schedule> scheduleOptional = scheduleMapper.findById(id);
        return scheduleOptional.orElse(null);
    }

    @Override
    public List<Schedule> findByName(String name) {
        // This method is not necessary as it is not used in the controller
        return null;
    }
    @Transactional
    @Override

    public Schedule addOne(Schedule schedule) {
        return scheduleMapper.save(schedule);
    }

    List<Schedule> findByStartDayBetween(String startDay, String endDay) {
        return scheduleMapper.findByStartDayBetween(startDay,endDay);
    }


    public void save(Schedule schedule) {
        scheduleMapper.save(schedule);
    }

    @Override
    public void deleteById(Long id) {
        scheduleMapper.deleteById(id);
    }

    @Override
    public Map<String, List<Schedule>> findDailySchedules() {
        List<Schedule> schedules = findAll();
        Map<String, List<Schedule>> dailySchedules = new HashMap<>();

        // 设定星期的顺序和映射
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
        Map<String, Integer> dayIndexMap = new HashMap<>();
        for (int i = 0; i < weekDays.length; i++) {
            dayIndexMap.put(weekDays[i], i);
        }

        for (Schedule schedule : schedules) {
            int startIndex;
            if(dailySchedules.get(schedule.getStartDay()) != null){
                startIndex = dayIndexMap.get(schedule.getStartDay());
            }else{
                startIndex = 0;
            }
            int endIndex;
            if(dayIndexMap.get(schedule.getEndDay())!=null){
                endIndex = dayIndexMap.get(schedule.getEndDay());
            }else{
                endIndex = 0;
            }
            // 处理跨星期的情况
            if (startIndex <= endIndex) {
                for (int i = startIndex; i <= endIndex; i++) {
                    dailySchedules.computeIfAbsent(weekDays[i], k -> new ArrayList<>()).add(schedule);
                }
            } else {
                for (int i = startIndex; i < weekDays.length; i++) {
                    dailySchedules.computeIfAbsent(weekDays[i], k -> new ArrayList<>()).add(schedule);
                }
                for (int i = 0; i <= endIndex; i++) {
                    dailySchedules.computeIfAbsent(weekDays[i], k -> new ArrayList<>()).add(schedule);
                }
            }
        }

        return dailySchedules;
    }

    @Override
    public byte[] exportSchedules() throws IOException {
        Workbook workbook = new XSSFWorkbook(); // 使用XSSFWorkbook创建Excel工作簿
        Sheet sheet = workbook.createSheet("Schedules");

        // 创建表头行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("End_Day");
        headerRow.createCell(2).setCellValue("Start_Day");

        // 获取数据并填充到Excel表格中
        List<Schedule> Schedules = scheduleMapper.findAll();
        int rowNum = 1;
        for (Schedule schedule : Schedules) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(schedule.getId());
            row.createCell(1).setCellValue(schedule.getEndDay());
            row.createCell(2).setCellValue(schedule.getStartDay());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }
}
