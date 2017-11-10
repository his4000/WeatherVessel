package com.capstonedesign.weathervessel.service.messaging;

import lombok.Data;

@Data
public class Photo {

    private String url;
    private final int width = 640;
    private final int height = 480;

    public Photo(String url){
        this.url = url;
    }
}
