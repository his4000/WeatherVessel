package com.capstonedesign.weathervessel;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class WeathervesselController {

    private String hello = "Project Weather Vessel for Capstone Design 2 class in Konkuk Univ. Department of Computer Science Engineering - KIM MIN SU, LEE CHANG OH, CHO YOON KI";

    @RequestMapping("/")
    public String hello(){
        return this.hello;
    }

    @RequestMapping("/keyboard")
    public String keyboard(){
        return "{\"type\" : \"buttons\", \"buttons\" : [\"선택 1\", \"선택 2\", \"선택 3\"]}";
    }
}
