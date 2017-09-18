package com.capstonedesign.weathervessel.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String classType;

    @Column
    private String text;

    public Questions(String classType, String text){
        this.classType = classType;
        this.text = text;
    }
}
