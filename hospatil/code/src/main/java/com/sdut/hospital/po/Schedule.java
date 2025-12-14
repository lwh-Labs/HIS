package com.sdut.hospital.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // other fields and methods

    @Column(name = "start_day", nullable = false)
    private String startDay;

    @Column(name = "end_day", nullable = false)
    private String endDay;


    // Ensure you have the necessary getters and setters

    public void setDoctorId(Long doctorId) {
        if (this.doctor == null) {
            this.doctor = new Doctor();
        }
        this.doctor.setId(doctorId);
    }

    public Long getDoctorId() {
        return this.doctor != null ? this.doctor.getId() : null;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public void setDoctorName(String doctorName) {
        if (this.doctor == null) {
            this.doctor = new Doctor();
        }
        this.doctor.setName(doctorName);
    }

    public String getDoctorName() {
        return this.doctor != null ? this.doctor.getName() : null;
    }
}
