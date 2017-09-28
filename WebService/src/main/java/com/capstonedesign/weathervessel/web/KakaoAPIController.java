package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.RequestMessage;
import com.capstonedesign.weathervessel.service.messaging.ResponseMessage;
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
public class KakaoAPIController {

    private String hello;

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
        log.info("make text" + content);

        log.info(requestMessage.toString());

        if(requestMessage.getContent().isEmpty()) {
            log.info("getContent is empty");
            responseMessage.setMessage(new Message("뭐라구요? 잘 안들려요"));
        }
        else {
            try {
                result = classifying(content);
            } catch (Exception e) {
                log.info("Exception occurred");
            }
        }

        if(result.equals("")) {
            responseMessage.setMessage(new Message("적당한 대답이 오지 않았어요"));
            log.info("No Answer");
        }
        else {
            if(result.equalsIgnoreCase("current"))
                responseMessage.setMessage(new Message("http://ec2-13-124-228-172.ap-northeast-2.compute.amazonaws.com:8090/currentView"));
            else
                responseMessage.setMessage(new Message(result));
            log.info("Answer : " + result);
        }

        return responseMessage;
    }

    private String classifying(String encodedContent) throws Exception{
        String textToken = URLEncoder.encode(encodedContent, "UTF-8");
        String learningServerURL = "http://ec2-52-78-148-146.ap-northeast-2.compute.amazonaws.com:8090/getText";
        String ret;
        URL url = new URL(learningServerURL);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(textToken);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();

        BufferedReader br;
        if(responseCode == 200)
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        else
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));

        ret = br.readLine();
        br.close();

        return ret;
    }
}
