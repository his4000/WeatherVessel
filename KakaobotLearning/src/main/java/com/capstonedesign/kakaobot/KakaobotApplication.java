package com.capstonedesign.kakaobot;

import com.capstonedesign.kakaobot.service.machine_learning.Keyword;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class KakaobotApplication {

	public static List<Keyword> keywords;

	public static void main(String[] args) {
		keywords = new ArrayList<>();
		SpringApplication.run(KakaobotApplication.class, args);
	}
}
