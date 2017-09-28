package com.capstonedesign.kakaobot.service.machine_learning;

import com.capstonedesign.kakaobot.service.natural_language_processing.NaturalLanguageProcessing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class Prediction {

    @Autowired
    NaturalLanguageProcessing naturalLanguageProcessing;
    @Autowired
    KeywordModel keywordModel;

    public String executePrediciton(List<Keyword> keywords, String text, boolean observe){
        List<String> tokens = naturalLanguageProcessing.extractPhrase(text);
        double[] probs = new double[Learning.numAttr];
        int maxIndex = 0;
        double maxProb = 0.0;

        tokens.stream()
                .filter(str -> keywordModel.hasKeyword(keywords, str))
                .forEach(str -> probs[keywordModel.getKeyword(keywords, str).getMaxAttr()] = keywordModel.getKeyword(keywords, str).getMaxProb());

        for(int i=0;i<Learning.numAttr;i++){
            if(probs[i] > maxProb){
                maxIndex = i;
                maxProb = probs[i];
            }
        }

        if(observe)
            Arrays.stream(probs).forEach(prob -> log.info(String.valueOf(prob)));

        if(maxIndex == Learning.FORECAST)
            return "forecase";
        if(maxIndex == Learning.CURRENT)
            return "current";
        else
            return "monitor";
    }
}
