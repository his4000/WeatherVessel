package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import com.capstonedesign.kakaobot.service.Brain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@Controller
@EnableAutoConfiguration
@Slf4j
public class ReplyController {

    @Autowired
    QuestionsRepository questionsRepository;
    Brain brain;

    @RequestMapping(value = "/getText", method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/getText", method = RequestMethod.POST)
    @ResponseBody
    public String getText(@RequestBody String textToken) throws Exception {
        brain = new Brain();
        log.info("get text token : " + textToken);
        String content;
        try{
            content = URLDecoder.decode(textToken, "UTF-8");
            log.info("Decoded content : " + content);
        }catch (Exception e){
            content = "";
            log.info("Exception occurred when decoding text token");
        }

        String result = brain.predictUnknownCase(content, false);

        log.info("result : " + result);

        return result;
        /*String replyService = brain.predictUnknownCase(text, false);

        jsonObject.put("replyText", replyService);
        questionsRepository.save(new Questions(replyService, text));
        return jsonObject;*/
    }
}