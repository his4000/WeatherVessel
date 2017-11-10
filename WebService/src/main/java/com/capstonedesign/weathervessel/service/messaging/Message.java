package com.capstonedesign.weathervessel.service.messaging;

import lombok.Data;

@Data
public class Message {
    private String text;
    private MessageButton message_button;
    private Photo photo;

    public Message(String text){
        this.text = text;
    }

    public Message(String text, MessageButton messageButton){
        this.text = text;
        this.message_button = messageButton;
    }

    public Message(String text, Photo photo){
        this.text = text;
        this.photo = photo;
    }

    public Message(String text, MessageButton messageButton, Photo photo){
        this.text = text;
        this.message_button = messageButton;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Message [text=" + text + "]";
    }
}
