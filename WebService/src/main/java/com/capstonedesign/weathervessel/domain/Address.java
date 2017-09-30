package com.capstonedesign.weathervessel.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String address;

    public Address(String address){
        this.address = address;
    }
}
