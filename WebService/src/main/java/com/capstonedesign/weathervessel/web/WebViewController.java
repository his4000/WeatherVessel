package com.capstonedesign.weathervessel.web;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping(value = "/mapView")
    public ModelAndView webViewing()
    {
        ModelAndView mv = new ModelAndView();

        mv.addObject("address", "서울특별시 광진구 화양동");
        mv.addObject("time", getNowTime());
        mv.setViewName("monitor");

        return mv;
    }

    @RequestMapping(value = "/currentView")
    public ModelAndView currentViewing(){
        ModelAndView mv = new ModelAndView();

        mv.addObject("address", "서울특별시 광진구 화양동");
        mv.addObject("time", getNowTime());
        mv.setViewName("current");
        return mv;
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

    private String getLatLng(String address) {
        String clientId = "eNueqceuff0oFPhe5uZD";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "sq1flv6Kxt";//애플리케이션 클라이언트 시크릿값";
        try {
            String addr = URLEncoder.encode(address, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            return "";
        }
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
