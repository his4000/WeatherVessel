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

    private String hello;
    private String learningServerURL;

    @RequestMapping("/")
    public String hello(){
        hello = "Project Weather Vessel for Capstone Design 2 class in Konkuk Univ. Department of Computer Science Engineering - KIM MIN SU, LEE CHANG OH, CHO YOON KI";
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
        String content = requestMessage.getContent();
        String textToken;
        learningServerURL = "http://ec2-52-78-20-141.ap-northeast-2.compute.amazonaws.com:8090/getText";
        log.info("make text" + content);

        log.info(requestMessage.toString());

        if(requestMessage.getContent().isEmpty()) {
            log.info("getContent is empty");
            responseMessage.setMessage(new Message("뭐라구요? 잘 안들려요"));
        }

        else {
            try {
                log.info("http start");
                textToken = URLEncoder.encode(content, "UTF-8");
                log.info("text token : " + textToken);
                URL url = new URL(learningServerURL);
                log.info("make url instance");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                log.info("make http connection");
                con.setRequestMethod("POST");
                log.info("set post");

                con.setDoOutput(true);
                log.info("set do output");
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                log.info("make output stream");
                wr.writeBytes(textToken);
                log.info("write bytes");
                wr.flush();
                log.info("flush");
                wr.close();
                log.info("close");

                int responseCode = con.getResponseCode();
                log.info("Response Code : " + String.valueOf(responseCode));

                BufferedReader br;
                log.info("make buffered reader");
                if(responseCode == 200) {
                    log.info("response code is 200");
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    log.info("make buffered reader for input");
                }
                else {
                    log.info("response code is others");
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    log.info("make buffered reader for error");
                }

                result = br.readLine();
                log.info("read line");
                br.close();
                log.info("closing");

            } catch (Exception e) {
                log.info("Exception occurred");
            }
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
