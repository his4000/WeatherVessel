package com.capstonedesign.kakaobot.web;

import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import com.capstonedesign.kakaobot.service.machine_learning.Learning;
import com.capstonedesign.kakaobot.service.natural_language_processing.NaturalLanguageProcessing;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
@Slf4j
public class LearningController {

    @Autowired
    QuestionsRepository questionsRepository;
    @Autowired
    Learning learning;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/learning", method = RequestMethod.GET)
    public String postLearning(@RequestParam("rdb") String radio, @RequestParam("text") String text){

        log.info(radio);
        log.info(text);

        questionsRepository.save(new Questions(radio, text));

        return "index";
    }

    @RequestMapping(value = "/modifyIndex", method = RequestMethod.GET)
    public String callModifyPage() {return "modifying";}

    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String getModify(@RequestParam("intent") String intent, @RequestParam("number") String number) {
        int numberInteger = Integer.parseInt(number);
        Questions question = questionsRepository.findOne(numberInteger);

        log.info("Modify controller called");

        question.setClassType(intent);
        questionsRepository.save(question);

        log.info("Success to modify");

        learning.getNotLearnedIndex().add(numberInteger);
        log.info("Add modified index : " + String.valueOf(numberInteger));
        learning.executeLearning();

        return "modifying";
    }
}
