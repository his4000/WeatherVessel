package com.capstonedesign.weathervessel.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
@Slf4j
public class WebViewController {

    @RequestMapping(value = "/mapView")
    public String webViewing(){
        return "monitor";
    }

    @RequestMapping(value = "/currentView")
    public String currentViewing(){
        return "current";
    }

    @RequestMapping(value = "/heatMapView")
    public String heatMapViewing(){
        return "heatMap";
    }
}
