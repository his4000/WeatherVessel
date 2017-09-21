package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.KakaobotApplication;
import com.capstonedesign.kakaobot.service.machine_learning.Prediction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@RestController
@EnableAutoConfiguration
@Slf4j
public class ReplyController {
    @Autowired
    private Prediction prediction;

    @RequestMapping(value = "/getText", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/getText", method = RequestMethod.POST)
    public String getText(@RequestBody String textToken) throws Exception {
        log.info("get text token : " + textToken);
        String content;
        try{
            content = URLDecoder.decode(textToken, "UTF-8");
            log.info("Decoded content : " + content);
        }catch (Exception e){
            content = "";
            log.info("Exception occurred when decoding text token");
        }

        String result = prediction.executePrediciton(KakaobotApplication.keywords, content, true);

        log.info("result : " + result);

        return result;
        /*String replyService = brain.predictUnknownCase(text, false);

        jsonObject.put("replyText", replyService);
        questionsRepository.save(new Questions(replyService, text));
        return jsonObject;*/
    }
}