package com.sdut.hospital.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "bed")
@Getter
@Setter
@NoArgsConstructor
public class Bed extends Date implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private Integer number;
    @JsonIgnoreProperties({"bed"})
    @OneToOne(mappedBy = "bed")
    private Patient patient;
}
