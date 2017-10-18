package com.capstonedesign.weathervessel.service.messaging;

import lombok.Data;

@Data
public class MessageButton {

    private final String label = "서비스 바로가기";
    private String url;

    public MessageButton(String url){
        this.url = url;
    }
}
