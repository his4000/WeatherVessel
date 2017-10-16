package com.capstonedesign.weathervessel.service.messaging;

import lombok.Data;

@Data
public class Message {
    private String text;
    private MessageButton message_button;

    public Message(String text){
        this.text = text;
    }

    public Message(String text, MessageButton messageButton){
        this.text = text;
        this.message_button = messageButton;
    }

    @Override
    public String toString() {
        return "Message [text=" + text + "]";
    }
}
