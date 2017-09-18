package com.capstonedesign.kakaobot.service;

import lombok.Data;

@Data
public class ResponseMessage {
    private Message message;

    @Override
    public String toString() {
        return "ResponseMessage [message=" + message + "]";
    }
}
