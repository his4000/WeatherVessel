package com.capstonedesign.weathervessel.web;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableAutoConfiguration
@Slf4j
public class MapAPIController {

    private String clientId = "eNueqceuff0oFPhe5uZD";  //Naver map api ID
    private String clientSecret = "sq1flv6Kxt";  //Naver map api secret

    @RequestMapping(value = "/reversegeocode")
    public String reverseGeocode(@RequestBody String gps) {
        JSONObject resultFromNaver = new JSONObject(getAddress(gps));
        JSONArray addressItems = resultFromNaver.getJSONObject("result").getJSONArray("items");
        JSONObject tmpJson;
        List<String> addressList = new ArrayList<>();

        for(int i=0;(tmpJson = addressItems.getJSONObject(i)) != null;i++){
            addressList.add(tmpJson.getJSONObject("addressdetail").get("dongmyun").toString());
        }

        return addressList.toString();
    }

    private String getLatLng(String address) {
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

    private String getAddress(String gps) {
        try {
            String addr = URLEncoder.encode(gps, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/map/reversegeocode?query=" + addr; //json
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
}
