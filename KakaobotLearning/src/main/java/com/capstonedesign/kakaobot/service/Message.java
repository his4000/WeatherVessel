package com.capstonedesign.kakaobot.service;

import lombok.Data;

@Data
public class Message {
    private String text;

    public Message(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message [text=" + text + "]";
    }
}
