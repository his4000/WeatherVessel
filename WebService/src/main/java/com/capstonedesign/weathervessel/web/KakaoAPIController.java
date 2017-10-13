package com.capstonedesign.weathervessel.web;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.Observe;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.MessageButton;
import com.capstonedesign.weathervessel.service.messaging.RequestMessage;
import com.capstonedesign.weathervessel.service.messaging.ResponseMessage;
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

    private final String hello = "Project Weather Vessel for Capstone Design 2 class in Konkuk Univ. Department of Computer Science Engineering - KIM MIN SU, LEE CHANG OH, CHO YOON KI";
    @Autowired
    NaturalLanguageProcessing naturalLanguageProcessing;
    @Autowired
    ObserveRepository observeRepository;
    @Autowired
    AddressRepository addressRepository;

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
            if(result.equalsIgnoreCase("current")) {
                List<KoreanTokenJava> addresses = naturalLanguageProcessing.filterAddress(naturalLanguageProcessing.textTokenizing(content));
                String url = "http://ec2-52-78-23-33.ap-northeast-2.compute.amazonaws.com:8090/currentView/";

                if(addresses.isEmpty())
                    responseMessage.setMessage(new Message("현재 서울 지역 미세먼지 정보만 제공하고 있어요. 서울 어디의 날씨가 궁금하세요?"));
                else if(addresses.size() > 1)
                    responseMessage.setMessage(new Message("하나의 지역만 입력해 주세요"));
                else {
                    String address = addresses.get(0).getText();
                    url = url + address;
                    responseMessage.setMessage(new Message(setReplyMessage(address), new MessageButton(url)));
                }
            }
            else
                responseMessage.setMessage(new Message(result));
            log.info("Answer : " + result);
        }

        return responseMessage;
    }

    private String setReplyMessage(String address){
        Observe observe = observeRepository.findObserveByAddrIdOrderByTimeDesc(addressRepository.findAddressByAddrDongLike(address)).get(0);
        Long pm10 = observe.getPm10();
        Long pm25 = observe.getPm25();

        switch (WebViewController.getStatus(pm10, pm25)){
            case VeryGood:
                return "오늘"+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 아주 상쾌한 날씨에요 ;)\n\n"
                        + "야외 나들이나 바깥활동을 계획해보시는게 어때요? :)\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
            case Good:
                return "오늘"+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 비교적 깨끗한 날씨에요 :)\n\n"
                        + "야외 활동을 하기에 적당한 날씨일 것 같네요 ;)\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
            case Bad:
                return "오늘"+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 미세먼지 상태가 좋지 않네요 ㅠㅠ\n\n"
                        + "야외 활동 시에는 꼭 마스크를 챙겨주세요~!\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
            case VeryBad:
                return "오늘"+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 아주 나쁜 상태에요 ㅠㅠ\n\n"
                        + "야외 활동을 삼가하시고 꼭 나가야 하는 경우에는 마스크를 반드시 챙기도록 하세요~!\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
                default:
                    return "정확한 관측 정보를 찾지 못한 것 같아요ㅠㅠ\n\n"
                            + "주소 정보 등이 정확한지 확인하시고 다시 시도해 주세요\n\n"
                            + "다시 시도해도 정보를 찾지 못할 경우 서비스 지역이 아닐 수 있어요";
        }
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
