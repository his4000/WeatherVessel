package com.capstonedesign.kakaobot.service.machine_learning;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class KeywordModel {

    public Keyword getKeyword(List<Keyword> keywords, String keyword) throws NoSuchElementException{
        return keywords.stream().filter(k -> k.getKeyword().equalsIgnoreCase(keyword)).findFirst().get();
    }

    public boolean hasKeyword(List<Keyword> keywords, String keyword){
        return keywords.stream().anyMatch(k -> k.getKeyword().equalsIgnoreCase(keyword));
    }

    public void addProbWithStr(List<Keyword> keywords, String keyword, String attrIndexStr){
        int attrIndex = -1;

        if(attrIndexStr.equalsIgnoreCase("forecast"))
            attrIndex = Learning.FORECAST;
        else if(attrIndexStr.equalsIgnoreCase("current"))
            attrIndex = Learning.CURRENT;
        else if(attrIndexStr.equalsIgnoreCase("monitor"))
            attrIndex = Learning.MONITOR;

        try{
            getKeyword(keywords, keyword).addProb(attrIndex);
        }catch (NoSuchElementException e){
            if(attrIndex != -1) {
                Keyword tmp = new Keyword(keyword);
                tmp.addProb(attrIndex);
                keywords.add(tmp);
            }
            else
                log.info("Undefined enum value in KeywordModel");
        }
    }
}
