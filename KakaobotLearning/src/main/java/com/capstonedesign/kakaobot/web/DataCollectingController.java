package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import com.capstonedesign.kakaobot.service.messaging.Message;
import com.capstonedesign.kakaobot.service.messaging.RequestMessage;
import com.capstonedesign.kakaobot.service.messaging.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
@Slf4j
public class DataCollectingController {

    @Autowired
    QuestionsRepository questionsRepository;

    @RequestMapping(value = "/keyboard", method = RequestMethod.GET)
    public String keyboard(){
        log.info("keyboard controller running");

        JSONObject keyboardObj = new JSONObject();
        keyboardObj.put("type", "text");

        return keyboardObj.toString();
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public ResponseMessage message(@RequestBody RequestMessage requestMessage){
        ResponseMessage responseMessage = new ResponseMessage();
        String content = requestMessage.getContent();

        if(content.equals(""))
            responseMessage.setMessage(new Message("질문이 도착하지 않았어요. 질문을 다시 보내주세요"));
        else {
            questionsRepository.save(new Questions("notype", content));
            responseMessage.setMessage(new Message("질문을 보내주셔서 정말 감사합니다."));
        }

        return responseMessage;
    }
}
