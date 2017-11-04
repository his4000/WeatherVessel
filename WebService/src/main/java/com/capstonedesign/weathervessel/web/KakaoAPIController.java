package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.Observe;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.messaging.*;
import com.capstonedesign.weathervessel.service.messaging.current.CurrentReply;
import com.capstonedesign.weathervessel.service.messaging.greeting.GreetingReply;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openkoreantext.processor.KoreanTokenJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@RestController
@EnableAutoConfiguration
@Slf4j
public class KakaoAPIController {

    @Autowired
    NaturalLanguageProcessing naturalLanguageProcessing;
    @Autowired
    ObserveRepository observeRepository;
    @Autowired
    AddressRepository addressRepository;

    @RequestMapping("/")
    public String hello(){
        return "Project Weather Vessel for Capstone Design 2 class in Konkuk Univ. Department of Computer Science Engineering - KIM MIN SU, LEE CHANG OH, CHO YOON KI";
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
            responseMessage.setMessage(new Message(
                    "메세지가 도달하지 못했어요(훌쩍)(훌쩍)\n\n"
                            + "관리자에게 문의해주세요(하하)(하하)"
            ));
        }
        else {
            try {
                result = classifying(content);
            } catch (Exception e) {
                log.info("Exception occurred in Classifying");
            }
        }

        if(result.equals("")) {
            responseMessage.setMessage(new Message(
                    "의도를 잘 파악하지 못했어요(훌쩍)(훌쩍)\n\n"
                    + "좀 더 상세히 말씀 해주시겠어요?(하하)(하하)"
            ));
            log.info("No Answer");
        }
        else {
            Reply reply;
            if(result.equalsIgnoreCase("greeting")){
                reply = new GreetingReply();
                responseMessage.setMessage(reply.getReplyMessage(content, naturalLanguageProcessing, observeRepository, addressRepository));
            }
            if(result.equalsIgnoreCase("current")) {
                reply = new CurrentReply();
                responseMessage.setMessage(reply.getReplyMessage(content, naturalLanguageProcessing, observeRepository, addressRepository));
            }
            else
                responseMessage.setMessage(new Message(result));
            log.info("Answer : " + result);
        }

        return responseMessage;
    }

    private String classifying(String encodedContent) throws Exception{
        String textToken = URLEncoder.encode(encodedContent, "UTF-8");
        String learningServerURL = "http://localhost:8091/getText";
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
