package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.service.RequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@Slf4j
public class WeathervesselController {

    private String hello = "Project Weather Vessel for Capstone Design 2 class in Konkuk Univ. Department of Computer Science Engineering - KIM MIN SU, LEE CHANG OH, CHO YOON KI";

    @RequestMapping("/")
    public String hello(){
        return this.hello;
    }

    @RequestMapping("/keyboard")
    public String keyboard(){
        log.info("keyboard controller running");
        return "{\"type\" : \"buttons\", \"buttons\" : [\"선택 1\", \"선택 2\", \"선택 3\"]}";
    }

    @RequestMapping("/message")
    public RequestMessage reply(String user_key) {
        log.info("message controller running");

        RequestMessage requestMessage = new RequestMessage();

        requestMessage.setUser_key(user_key);
        requestMessage.setType("text");
        requestMessage.setContent("잘 들립니다.");

        return requestMessage;
    }
}
