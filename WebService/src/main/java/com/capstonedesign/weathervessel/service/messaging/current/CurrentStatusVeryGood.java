package com.capstonedesign.weathervessel.service.messaging.current;

import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.MessageButton;

public class CurrentStatusVeryGood implements CurrentStatus {

    public Message getReplyText(String pm10, String pm25, String address, String url){
        return new Message(
                "오늘 "+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 아주 상쾌한 날씨에요 (신나)(신나)\n\n"
                        + "야외 나들이나 바깥활동을 계획해보시는게 어때요? (신나)\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요",
                new MessageButton(url)
        );
    }
}
