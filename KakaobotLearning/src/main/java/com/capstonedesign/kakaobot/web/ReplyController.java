package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import com.capstonedesign.kakaobot.service.Brain;
import lombok.extern.slf4j.Slf4j;
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
public class ReplyController {

    @Autowired
    QuestionsRepository questionsRepository;

    @RequestMapping(value = "/getText", method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/getText", method = RequestMethod.POST)
    @ResponseBody
    public String getText(@RequestBody String text) throws Exception {
        Brain brain = new Brain();
        log.info("get text : " + text);
        return brain.predictUnknownCase(text, false);
        /*String replyService = brain.predictUnknownCase(text, false);

        jsonObject.put("replyText", replyService);
        questionsRepository.save(new Questions(replyService, text));
        return jsonObject;*/
    }
}