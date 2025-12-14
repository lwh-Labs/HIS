package com.sdut.hospital.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends Date implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String password;
    private String phone;
    private String addr;
    private String avatar;
    @Temporal(TemporalType.DATE)
    private Date creatDate = new Date(System.currentTimeMillis());
    @Temporal(TemporalType.DATE)
    private Date updateDate;
    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private Set<Patient> patients = new HashSet<>();
    private boolean active;
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();
}
