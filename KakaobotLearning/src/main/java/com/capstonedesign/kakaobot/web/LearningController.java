package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/learning")
public class LearningController {

    @Autowired
    QuestionsRepository questionsRepository;

    @RequestMapping(method = RequestMethod.POST)
    public String index(){
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String postLearning(@RequestParam("rdb") String radio, @RequestParam("text") String text){

        questionsRepository.save(new Questions(radio, text));

        return "index";
    }
}
