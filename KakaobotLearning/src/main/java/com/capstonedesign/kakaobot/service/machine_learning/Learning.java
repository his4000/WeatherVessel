package com.capstonedesign.kakaobot.service.machine_learning;

import com.capstonedesign.kakaobot.KakaobotApplication;
import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Learning {

    @Autowired
    QuestionsRepository questionsRepository;
    @Autowired
    KeywordModel keywordModel;
    @Autowired
    NaturalLanguageProcessing naturalLanguageProcessing;

    private static final long batchTime = 1;

    public static final int numAttr = 3;
    public static final int FORECAST = 0;
    public static final int CURRENT = 1;
    public static final int MONITOR = 2;

    private int count;

    public Learning() {this.count = 0;}

    @Scheduled(fixedRate = batchTime * 60 * 1000)
    public void executeLearning() {
        List<Questions> questions = questionsRepository.findByIdGreaterThan(count);

        for(Questions question : questions){
            List<String> tokens = naturalLanguageProcessing.extractPhrase(question.getText());

            tokens.stream().forEach(str -> keywordModel.addProbWithStr(KakaobotApplication.keywords, str, question.getClassType()));
        }
    }
}
