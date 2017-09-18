package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import com.capstonedesign.kakaobot.service.Message;
import com.capstonedesign.kakaobot.service.RequestMessage;
import com.capstonedesign.kakaobot.service.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
@Slf4j
public class DataCollectingController {

    @Autowired
    QuestionsRepository questionsRepository;

    @RequestMapping(value = "/keyboard", method = RequestMethod.GET)
    @ResponseBody
    public String keyboard(){
        log.info("keyboard controller running");

        JSONObject keyboardObj = new JSONObject();
        keyboardObj.put("type", "text");

        return keyboardObj.toString();
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
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
