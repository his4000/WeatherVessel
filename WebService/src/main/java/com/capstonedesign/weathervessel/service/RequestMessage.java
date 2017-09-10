package com.capstonedesign.weathervessel.service;

import lombok.Data;

@Data
public class RequestMessage {
    private String user_key;
    private String type;
    private String content;
}
