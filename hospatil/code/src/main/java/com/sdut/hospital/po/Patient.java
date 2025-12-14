package com.sdut.hospital.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "patient")
@NoArgsConstructor
@Getter
@Setter
public class Patient implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phone;
    private String addr;
    private String avatar;
    private String password;
    private Float drugsprice;



    private boolean active;
    @Temporal(TemporalType.DATE)
    private Date creatDate = new Date(System.currentTimeMillis());
    @Temporal(TemporalType.DATE)
    private Date updateDate;
    @JsonIgnoreProperties({"patient"})
    @OneToOne
    private Bed bed;
    @ManyToOne
    private Doctor doctor;
    @JsonIgnore
    @OneToMany(mappedBy = "patient",cascade=CascadeType.REMOVE)
    private Set<Drug> drugs = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "patient",cascade=CascadeType.REMOVE)
    private Set<Examination> examinations = new HashSet<>();
    private Date createDate;
    private boolean discharged;


    public Patient(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getDischarged() {
        return discharged;
    }


}
