package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.domain.Address;
import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.Observe;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.WeatherStatus;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@EnableAutoConfiguration
@Slf4j
public class WebViewController {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ObserveRepository observeRepository;

    /*@RequestMapping(value = "/mapView/{address}")
    public ModelAndView webViewing(@PathVariable String address) {
        ModelAndView mv = new ModelAndView();
        Address wantedAddress = addressRepository.findAddressByAddrDongLike(address);
        List<Observe> observeList = observeRepository.findObserveByAddrIdOrderByTimeDesc(wantedAddress);

        mv.addObject("addressName", address);
        mv.addObject("address", wantedAddress.toString());
        mv.addObject("time", getNowTime());
        mv.addObject("observe", observeList.get(0));
        mv.setViewName("monitor");

        return mv;
    }*/

    @RequestMapping(value = "/currentView/{address}")
    public ModelAndView currentViewing(@PathVariable String address){
        ModelAndView mv = new ModelAndView();
        Address wantedAddress = addressRepository.findAddressByAddrDongLike(address);
        List<Observe> observeList = observeRepository.findObserveByAddrIdOrderByTimeDesc(wantedAddress);

        mv.addObject("addressName", address);
        mv.addObject("address", wantedAddress.toString());
        mv.addObject("time", getNowTime());
        mv.addObject("observe", observeList.get(0));

        Long pm10 = observeList.get(0).getPm10();
        Long pm25 = observeList.get(0).getPm25();

        switch (getStatus(pm10, pm25)){
            case VeryGood:
                mv.setViewName("currentVeryGood");
                break;
            case Good:
                mv.setViewName("currentGood");
                break;
            case Bad:
                mv.setViewName("currentBad");
                break;
            case VeryBad:
                mv.setViewName("currentVeryBad");
                break;
        }
        return mv;
    }

    public static WeatherStatus getStatus(Long pm10, Long pm25){
        if(pm10 < 50 && pm25 < 25){
            if(pm10 < 25 || pm25 < 12)
                return WeatherStatus.VeryGood;
            else
                return WeatherStatus.Good;
        }
        else{
            if(pm10 > 75 || pm25 > 38)
                return WeatherStatus.VeryBad;
            else
                return WeatherStatus.Bad;
        }
    }

    @RequestMapping(value = "/heatMapView")
    public ModelAndView heatMapViewing(){
        ModelAndView mv = new ModelAndView();
        Random random = new Random();
        List<Integer> pmList = new ArrayList<>();

        for(int i=0;i<39;i++)
            pmList.add(random.nextInt(100)+1);

        mv.addObject("pmList", pmList);
        mv.setViewName("heatMap");

        return mv;
    }

    private String getNowTime(){
        LocalDateTime nowTime = LocalDateTime.now();
        String year;
        String month;
        String day;
        String hour;
        String min;
        boolean afternoon;
        String time;

        year = String.valueOf(nowTime.getYear());

        if(nowTime.getMonthValue() > 9)
            month = String.valueOf(nowTime.getMonthValue());
        else
            month = "0" + String .valueOf(nowTime.getMonthValue());

        if(nowTime.getDayOfMonth() > 9)
            day = String.valueOf(nowTime.getDayOfMonth());
        else
            day = "0" + String.valueOf(nowTime.getDayOfMonth());

        if(nowTime.getHour() > 12){
            afternoon = true;
            if(nowTime.getHour() > 21)
                hour = String.valueOf(nowTime.getHour() - 12);
            else
                hour = "0" + String.valueOf(nowTime.getHour() - 12);
        }
        else{
            afternoon = false;
            if(nowTime.getHour() > 9)
                hour = String.valueOf(nowTime.getHour());
            else
                hour = "0" + String.valueOf(nowTime.getHour());
        }

        if(nowTime.getMinute() > 9)
            min = String.valueOf(nowTime.getMinute());
        else
            min = "0" + String.valueOf(nowTime.getMinute());

        time = year + "-" + month + "-" + day + " " + hour + ":" + min + " ";

        if(afternoon)
            time = time + "PM";
        else
            time = time + "AM";

        return time;
    }
}
