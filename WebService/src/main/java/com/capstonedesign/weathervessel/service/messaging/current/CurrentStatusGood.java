package com.capstonedesign.weathervessel.service.messaging.current;

import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.MessageButton;

public class CurrentStatusGood implements CurrentStatus {

    public Message getReplyText(String pm10, String pm25, String address, String url){
        return new Message(
                "오늘 "+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 비교적 깨끗한 날씨에요 (하하)(하하)\n\n"
                        + "야외 활동을 하기에 적당한 날씨일 것 같네요 (하하)\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요",
                new MessageButton(url)
        );
    }
}
