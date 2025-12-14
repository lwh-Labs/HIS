package com.sdut.hospital.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "drug")
@Getter
@Setter
@NoArgsConstructor
public class Drug implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String drugname;

    public String getDrugname(String drugname) {
        return this.drugname;
    }

    private Integer number;
    private Float price;
    private boolean isfinished=false;
    @Temporal(TemporalType.DATE)
    private Date creatDate = new Date(System.currentTimeMillis());
    @ManyToOne
    private Patient patient;

    public boolean getIsfinished() {
        return isfinished;
    }
}
