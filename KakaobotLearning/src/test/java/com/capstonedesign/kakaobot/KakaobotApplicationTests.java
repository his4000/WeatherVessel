package com.capstonedesign.kakaobot;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@Slf4j
public class KakaobotApplicationTests {

	@Test
	public void contextLoads() {
		log.info("Test Running");
	}

}
