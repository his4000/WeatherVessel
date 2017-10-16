package com.capstonedesign.weathervessel.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Observe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone droneId;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address addrId;

    @Column
    private LocalDateTime time;

    @Column
    private Double gps_x;

    @Column
    private Double gps_y;

    @Column
    private Long pm10;

    @Column
    private Long pm25;

    public Observe(){}
}
