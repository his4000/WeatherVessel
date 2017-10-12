package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.domain.Address;
import com.capstonedesign.weathervessel.domain.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/getAddressCode")
public class MapAPIController {

    @Autowired
    AddressRepository addressRepository;
    private String clientId = "eNueqceuff0oFPhe5uZD";  //Naver map api ID
    private String clientSecret = "sq1flv6Kxt";  //Naver map api secret

    @RequestMapping(value = "{gps}", method = RequestMethod.GET)
    public String reverseGeocode(@PathVariable String gps) {
        log.info("get parameter : " + gps);
        JSONObject resultFromNaver = new JSONObject(getAddress(gps));
        log.info(resultFromNaver.toString());
        JSONArray addressItems = resultFromNaver.getJSONObject("result").getJSONArray("items");
        List<String> addressList = new ArrayList<>();

        int i=0;
        while(true){
            try{
                addressList.add(addressItems.getJSONObject(i).getJSONObject("addrdetail").get("dongmyun").toString());
                i++;
            }catch(JSONException e){
                break;
            }
        }

        Address ret = null;

        for(String address : addressList){
            try{
                log.info(address);
                ret = addressRepository.findAddressByAddrDongLike(address);
                log.info(ret.toString());
                break;
            }catch(NullPointerException e){}
        }

        if(ret == null)
            return "null";
        else
            return String.valueOf(ret.getAddrId());
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
