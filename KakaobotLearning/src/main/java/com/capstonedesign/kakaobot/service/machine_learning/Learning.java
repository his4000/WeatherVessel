package com.capstonedesign.kakaobot.service.machine_learning;

import com.capstonedesign.kakaobot.domain.Questions;
import com.capstonedesign.kakaobot.domain.QuestionsRepository;
import com.capstonedesign.kakaobot.service.natural_language_processing.NaturalLanguageProcessing;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Getter
public class Learning{

    @Autowired
    QuestionsRepository questionsRepository;
    @Autowired
    KeywordModel keywordModel;
    @Autowired
    NaturalLanguageProcessing naturalLanguageProcessing;

    private static final long batchTime = 1;

    public static List<Keyword> keywords;

    public static final int numAttr = 3;
    public static final int FORECAST = 0;
    public static final int CURRENT = 1;
    public static final int MONITOR = 2;

    private List<Integer> notLearnedIndex;

    @PostConstruct
    public void initLearning(){
        keywords = new ArrayList<>();
        notLearnedIndex = new ArrayList<>();
        long count = questionsRepository.countAllBy();

        for(int i=1;i<=count;i++){
            notLearnedIndex.add(i);
        }
    }

    @Scheduled(fixedRate = batchTime * 60 * 1000)
    public void executeLearning() {
        int amountOfNotLearnedIndex = notLearnedIndex.size();
        log.info("Amount of not learned index : " + String.valueOf(amountOfNotLearnedIndex));

        if(amountOfNotLearnedIndex > 0) {
            List<Questions> questions = new ArrayList<>();

            notLearnedIndex.stream().forEach(i -> questions.add(questionsRepository.findById(i)));

            notLearnedIndex.clear();

            for (Questions question : questions) {
                List<String> tokens = naturalLanguageProcessing.extractPhrase(question.getText());

                tokens.stream().forEach(str -> keywordModel.addProbWithStr(keywords, str, question.getClassType()));
            }
        }
    }
}
