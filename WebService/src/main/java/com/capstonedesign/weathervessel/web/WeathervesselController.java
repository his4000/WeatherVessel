package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.service.Message;
import com.capstonedesign.weathervessel.service.RequestMessage;
import com.capstonedesign.weathervessel.service.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

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

        if(requestMessage.getContent().isEmpty())
            responseMessage.setMessage(new Message("뭐라구요? 잘 안들려요"));
        else
            responseMessage.setMessage(new Message("잘 들려요"));

        return responseMessage;
    }

    /*@RequestMapping(value = "/message", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public String reply(@RequestBody JSONObject resObj) throws Exception {
        log.info("message controller running");
        log.info("resObj : " + resObj.toString());

        String content;
        content = (String) resObj.get("content");
        JSONObject jsonRes = new JSONObject();
        JSONObject jsonText = new JSONObject();

        if(content != null)
            jsonText.put("text", "잘 들려요");
        else
            jsonText.put("text", "뭐라구요? 잘 안들려요");
        jsonRes.put("message", jsonText);

        return jsonRes.toString();
    }*/
}
