package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.domain.Questions;
import com.capstonedesign.weathervessel.domain.QuestionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@EnableAutoConfiguration
@Slf4j
public class DataMonitoringController {

    @Autowired
    QuestionsRepository questionsRepository;

    @RequestMapping(value = "/monitorPage")
    public ModelAndView monitoring(){
        ModelAndView mv = new ModelAndView();
        List<Questions> questions = questionsRepository.findAll();
        log.info(questions.toString());
        mv.addObject("questions", questions);
        mv.setViewName("monitor");
        return mv;
    }
}
