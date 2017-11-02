package com.capstonedesign.weathervessel.service.messaging.current;

import com.capstonedesign.weathervessel.service.messaging.Message;

public interface CurrentStatus {
    Message getReplyText(String pm10, String pm25, String address, String url);
}
