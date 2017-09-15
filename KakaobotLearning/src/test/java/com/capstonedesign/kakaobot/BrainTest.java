package com.capstonedesign.kakaobot;

import com.capstonedesign.kakaobot.service.Brain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@Slf4j
public class BrainTest {

    Brain brain;

    @Before
    public void learning(){
        brain = new Brain();

        try{
            brain.refresh("forecast", "오늘 날씨가 어때");
        } catch (Exception e) {
            log.info("Exception occurred in @Before");
        }
    }

    @Test
    public void predictUnknownCaseTest(){
        try {
            Assert.assertEquals("forecast", brain.predictUnknownCase("오늘 날씨가 어때", false));
            Assert.assertNotEquals("current", brain.predictUnknownCase("오늘 날씨가 어때", false));
            Assert.assertNotEquals("monitoring", brain.predictUnknownCase("오늘 날씨가 어때", false));
        } catch (Exception e) {
            log.info("Exception occurred in @Test");
        }
    }
}
