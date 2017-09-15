package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.service.Message;
import com.capstonedesign.weathervessel.service.RequestMessage;
import com.capstonedesign.weathervessel.service.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RestController
@EnableAutoConfiguration
@Slf4j
public class WeathervesselController {

    private String hello = "Project Weather Vessel for Capstone Design 2 class in Konkuk Univ. Department of Computer Science Engineering - KIM MIN SU, LEE CHANG OH, CHO YOON KI";

    @RequestMapping("/")
    public String hello(){
        return this.hello;
    }

    @RequestMapping(value = "/keyboard", method = RequestMethod.GET)
    public String keyboard(){
        log.info("keyboard controller running");

        JSONObject keyboardObj = new JSONObject();
        keyboardObj.put("type", "text");

        return keyboardObj.toString();
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public ResponseMessage message(@RequestBody RequestMessage requestMessage){
        ResponseMessage responseMessage = new ResponseMessage();
        String result = "";

        log.info(requestMessage.toString());

        if(requestMessage.getContent().isEmpty()) {
            log.info("getContent is empty");
            responseMessage.setMessage(new Message("뭐라구요? 잘 안들려요"));
        }

        else {
            try {
                String text = URLEncoder.encode(requestMessage.getContent(), "UTF-8");
                String apiURL = "http://localhost:8091/getText";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(text);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                log.info("Response Code : " + String.valueOf(responseCode));

                BufferedReader br;
                if(responseCode == 200)
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                else
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));

                result = br.readLine();
                br.close();

            } catch (Exception e) {}
        }

        if(result.equals("")) {
            responseMessage.setMessage(new Message("적당한 대답이 오지 않았어요"));
            log.info("No Answer");
        }
        else {
            responseMessage.setMessage(new Message(result));
            log.info(result);
        }

        return responseMessage;
    }
}
