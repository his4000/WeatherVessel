package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import com.capstonedesign.kakaobot.service.Brain;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyController {

    @Autowired
    QuestionsRepository questionsRepository;

    @RequestMapping(value = "/getText", method = RequestMethod.POST)
    public JSONObject getText(@RequestBody String text) throws Exception {
        Brain brain = new Brain();
        JSONObject jsonObject = new JSONObject();
        String replyService = brain.predictUnknownCase(text, false);

        jsonObject.put("replyText", replyService);
        questionsRepository.save(new Questions(replyService, text));
        return jsonObject;
    }
}