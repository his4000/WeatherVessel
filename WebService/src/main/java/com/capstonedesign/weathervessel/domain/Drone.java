package com.capstonedesign.weathervessel.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "drone_id")
    private Integer droneId;

    @Column(unique = true)
    private String droneName;
}
