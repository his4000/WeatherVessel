package com.capstonedesign.weathervessel.service.messaging.current;

import com.capstonedesign.weathervessel.service.messaging.Message;

public class CurrentStatusNoData implements CurrentStatus {

    public Message getReplyText(String pm10, String pm25, String address, String url){
        return new Message(
                "해당하는 장소의 미세먼지 데이터가 없어요 (훌쩍)(훌쩍)\n\n"
                        + "장소를 잘못 입력하셨거나 아직 드론이 활동하지 않는 장소인 것 같아요\n\n"
                        + "다른 장소를 입력하시거나 관리자에게 문의해주세요 (신나)(신나)"
        );
    }
}
