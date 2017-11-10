package com.capstonedesign.weathervessel.service.messaging;

import lombok.Data;

@Data
public class MessageButton {

    private String label = "서비스 바로가기";
    private String url;

    public MessageButton(String url){
        this.label = "서비스 바로가기";
        this.url = url;
    }

    public MessageButton(String url, String label){
        this.url = url;
        this.label = label;
    }
}
