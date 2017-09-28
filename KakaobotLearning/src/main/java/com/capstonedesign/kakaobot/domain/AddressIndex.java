package com.capstonedesign.kakaobot.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class AddressIndex {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String address;

    public AddressIndex(String address){
        this.address = address;
    }
}
