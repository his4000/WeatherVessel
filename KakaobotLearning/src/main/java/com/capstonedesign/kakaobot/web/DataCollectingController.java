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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Random;

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
        String replyText = "질문을 보내주셔서 정말 감사합니다. \n\n 답례로 좋은 글 하나 소개해 드릴게요.\n\n";
        String messageFilePath = "../../src/main/resources/static/replyMessage/text";
        Random random = new Random();
        int randomNumber = random.nextInt(50) + 1;

        messageFilePath = messageFilePath + String.valueOf(randomNumber) + ".txt";
        log.info("randomNumber : " + String.valueOf(randomNumber));

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(messageFilePath), "UTF8"))){
            replyText = replyText + "[" + String.valueOf(randomNumber) + "/50]\n" + br.readLine() + "\n\n감사합니다.";
            log.info(replyText);
            br.close();
        }catch (Exception e){
            log.info("Reply message load error.\n");
            e.printStackTrace();
            replyText = replyText + "[51/50] 자기 자신이 해낸 것을 즐기는 그리고 자기 자신이 하고 있는 것을 즐기는 사람은 행복한 사람이다. - 괴테" + "\n\n감사합니다.";
        }

        if(content.equals(""))
            responseMessage.setMessage(new Message("질문이 도착하지 않았어요. 질문을 다시 보내주세요"));
        else {
            questionsRepository.save(new Questions("notype", content));
            responseMessage.setMessage(new Message(replyText));
        }

        return responseMessage;
    }
}
