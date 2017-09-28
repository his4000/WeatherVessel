package com.capstonedesign.kakaobot;

import com.capstonedesign.kakaobot.service.natural_language_processing.NaturalLanguageProcessing;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@Slf4j
public class NaturalLanguageProcessingTest {

    @Autowired
    NaturalLanguageProcessing naturalLanguageProcessing;

    @Before
    public void init(){
        naturalLanguageProcessing.initNLP();
    }

    @Test
    public void textTokenizingTest(){
        log.info(naturalLanguageProcessing.textTokenizing("서울 광진구 화양동").toString());
    }
}
