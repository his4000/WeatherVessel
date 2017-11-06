package com.capstonedesign.weathervessel.service.messaging.current;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.DroneRepository;
import com.capstonedesign.weathervessel.domain.Observe;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.Reply;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;
import com.capstonedesign.weathervessel.web.WebViewController;
import org.openkoreantext.processor.KoreanTokenJava;

import java.util.ArrayList;
import java.util.List;

public class CurrentReply implements Reply {

    @Override
    public Message getReplyMessage(String content, NaturalLanguageProcessing naturalLanguageProcessing, ObserveRepository observeRepository, AddressRepository addressRepository, DroneRepository droneRepository){
        List<KoreanTokenJava> addresses = naturalLanguageProcessing.filterAddress(naturalLanguageProcessing.textTokenizing(content));

        if(!isToday(content))
            return new Message("지금은 현재 미세먼지 정보만 제공하고 있어요.(훌쩍)(훌쩍)\n\n"
                    + "현재의 미세먼지 값을 알고 싶은 위치를 ~동 과 함께 입력해주세요(하하)(하하)");

        if(addresses.isEmpty())
            return new Message("현재 서울 지역 미세먼지 정보만 제공하고 있어요. 서울 어디의 날씨가 궁금하세요?");
        else if(addresses.size() > 1)
            return new Message("하나의 지역만 입력해 주세요");
        else
            return setReplyMessage(addresses.get(0).getText(), observeRepository, addressRepository);
    }

    private Message setReplyMessage(String address, ObserveRepository observeRepository, AddressRepository addressRepository){
        CurrentStatus currentStatus;
        String url = "http://ec2-13-124-179-202.ap-northeast-2.compute.amazonaws.com:8090/currentView/" + address;
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

    private boolean isToday(String content){
        List<String> notTodayList = new ArrayList<>();

        notTodayList.add("내일");
        notTodayList.add("모레");
        notTodayList.add("낼모레");
        notTodayList.add("어제");
        notTodayList.add("그저께");
        notTodayList.add("그제");
        notTodayList.add("엊그제");
        notTodayList.add("엊그저께");
        notTodayList.add("내년");
        notTodayList.add("작년");

        for(String tmp : notTodayList){
            if(content.contains(tmp))
                return false;
        }

        return true;
    }
}
