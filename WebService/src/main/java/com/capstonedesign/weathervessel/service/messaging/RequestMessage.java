package com.capstonedesign.weathervessel.service.messaging;

public class RequestMessage {
    private String user_key;
    private String type;
    private String content;

    public String getContent(){
        return this.content;
    }

    @Override
    public String toString() {
        return "RequestMessage [user_key=" + user_key + ", type=" + type + ", content=" + content + "]";
    }
}
