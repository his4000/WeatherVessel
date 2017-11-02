package com.capstonedesign.weathervessel.service.messaging.current;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.Observe;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.Reply;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;
import com.capstonedesign.weathervessel.web.WebViewController;
import org.openkoreantext.processor.KoreanTokenJava;

import java.util.List;

public class CurrentReply implements Reply {

    @Override
    public Message getReplyMessage(String content, NaturalLanguageProcessing naturalLanguageProcessing, ObserveRepository observeRepository, AddressRepository addressRepository){
        List<KoreanTokenJava> addresses = naturalLanguageProcessing.filterAddress(naturalLanguageProcessing.textTokenizing(content));

        if(addresses.isEmpty())
            return new Message("현재 서울 지역 미세먼지 정보만 제공하고 있어요. 서울 어디의 날씨가 궁금하세요?");
        else if(addresses.size() > 1)
            return new Message("하나의 지역만 입력해 주세요");
        else
            return setReplyMessage(addresses.get(0).getText(), observeRepository, addressRepository);
    }

    private Message setReplyMessage(String address, ObserveRepository observeRepository, AddressRepository addressRepository){
        CurrentStatus currentStatus;
        String url = "http://ec2-52-78-23-33.ap-northeast-2.compute.amazonaws.com:8090/currentView/" + address;
        List<Observe> observeList = observeRepository.findObserveByAddrIdOrderByTimeDesc(addressRepository.findAddressByAddrDongLike(address));
        if(observeList.size() == 0){
            currentStatus = new CurrentStatusNoData();
            return currentStatus.getReplyText(null, null, null, null);
        }
        Observe observe = observeList.get(0);
        Long pm10 = observe.getPm10();
        Long pm25 = observe.getPm25();

        switch (WebViewController.getStatus(pm10, pm25)){
            case VeryGood:
                currentStatus = new CurrentStatusVeryGood();    break;
            case Good:
                currentStatus = new CurrentStatusGood();        break;
            case Bad:
                currentStatus = new CurrentStatusBad();         break;
            case VeryBad:
                currentStatus = new CurrentStatusVeryBad();     break;
            default:
                currentStatus = null;                           break;
        }

        return currentStatus.getReplyText(String.valueOf(pm10), String.valueOf(pm25), address, url);
    }
}
