package com.capstonedesign.weathervessel.service.messaging;

import lombok.Data;

@Data
public class ResponseMessage {
    private Message message;

    @Override
    public String toString() {
        return "ResponseMessage [message=" + message + "]";
    }
}
