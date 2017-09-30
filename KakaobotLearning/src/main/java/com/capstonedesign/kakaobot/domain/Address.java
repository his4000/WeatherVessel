package com.capstonedesign.kakaobot.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private Integer addrId;

    @Column(unique = true)
    private String addrDong;

    @Column
    private String addrSi;

    @Column
    private String addrGu;

    public Address() {}

    public Address(String address_si, String address_gu, String address_dong){
        this.addrSi = address_si;
        this.addrGu = address_gu;
        this.addrDong = address_dong;
    }
}
