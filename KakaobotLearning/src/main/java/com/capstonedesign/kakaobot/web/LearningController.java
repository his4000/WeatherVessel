package com.capstonedesign.kakaobot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/learning")
public class LearningController {

    @RequestMapping(method = RequestMethod.POST)
    public String postLearning(Model model){
        return "index";
    }
}
