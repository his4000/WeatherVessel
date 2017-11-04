package com.capstonedesign.weathervessel.service.messaging.greeting;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.DroneRepository;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.Photo;
import com.capstonedesign.weathervessel.service.messaging.Reply;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;

public class GreetingReply implements Reply {

    private final String greetingText = "안녕하세요.\n" +
            "2017 건국대학교 소프트웨어융합학부 졸업 작품 Weather Vessel 팀 입니다.\n" +
            "카카오톡 플러스 친구 '날씨 드론'을 이용하여 현재 미세먼지 측정 정보를 제공하는 Chatbot 입니다.";

    public Message getReplyMessage(String content, NaturalLanguageProcessing naturalLanguageProcessing, ObserveRepository observeRepository, AddressRepository addressRepository, DroneRepository droneRepository){
        return new Message(greetingText, new Photo("http://localhost/Logo.png"));
    }
}
