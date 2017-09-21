package com.capstonedesign.weathervessel.web;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@Slf4j
public class GeocodeController {

    @RequestMapping(value = "/geocode")
    public String coordToAddress(@RequestBody JSONObject param) {
        log.info(param.getString("content"));

        return "hello";
    }
}
