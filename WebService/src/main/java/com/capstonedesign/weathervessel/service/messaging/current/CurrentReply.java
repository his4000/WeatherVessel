package com.capstonedesign.weathervessel.service.messaging.current;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.Observe;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.MessageButton;
import com.capstonedesign.weathervessel.service.messaging.Reply;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;
import com.capstonedesign.weathervessel.web.WebViewController;
import org.openkoreantext.processor.KoreanTokenJava;
import org.springframework.beans.factory.annotation.Autowired;

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
        //String message;
        String url = "http://ec2-52-78-23-33.ap-northeast-2.compute.amazonaws.com:8090/currentView/" + address;
        List<Observe> observeList = observeRepository.findObserveByAddrIdOrderByTimeDesc(addressRepository.findAddressByAddrDongLike(address));
        if(observeList.size() == 0){
            /*message =  "해당하는 장소의 미세먼지 데이터가 없어요 (훌쩍)(훌쩍)\n\n"
                    + "장소를 잘못 입력하셨거나 아직 드론이 활동하지 않는 장소인 것 같아요\n\n"
                    + "다른 장소를 입력하시거나 관리자에게 문의해주세요 (신나)(신나)";
            return new Message(message);*/
            currentStatus = new CurrentStatusNoData();
            return currentStatus.getReplyText(null, null, null, null);
        }
        Observe observe = observeList.get(0);
        Long pm10 = observe.getPm10();
        Long pm25 = observe.getPm25();

        switch (WebViewController.getStatus(pm10, pm25)){
            /*case VeryGood:
                message = "오늘 "+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 아주 상쾌한 날씨에요 (신나)(신나)\n\n"
                        + "야외 나들이나 바깥활동을 계획해보시는게 어때요? (신나)\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
                return new Message(message, new MessageButton(url));
            case Good:
                message = "오늘 "+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 비교적 깨끗한 날씨에요 (하하)(하하)\n\n"
                        + "야외 활동을 하기에 적당한 날씨일 것 같네요 (하하)\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
                return new Message(message, new MessageButton(url));
            case Bad:
                message =  "오늘 "+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 미세먼지 상태가 좋지 않네요 (훌쩍)(훌쩍)\n\n"
                        + "야외 활동 시에는 꼭 마스크를 챙겨주세요~!\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
                return new Message(message, new MessageButton(url));
            case VeryBad:
                message =  "오늘 "+ address + "의 상태는 미세먼지 " + String.valueOf(pm10) + "ug/m3, 초미세먼지 " + String.valueOf(pm25) + "ug/m3으로 아주 나쁜 상태에요 (헉)(헉)\n\n"
                        + "야외 활동을 삼가하시고 꼭 나가야 하는 경우에는 마스크를 반드시 챙기도록 하세요~!\n\n"
                        + "더 자세한 미세먼지 정보를 보시려면 아래 버튼을 눌러주세요";
                return new Message(message, new MessageButton(url));
            default:
                message = "정확한 관측 정보를 찾지 못한 것 같아요 (훌쩍)(훌쩍)\n\n"
                        + "주소 정보 등이 정확한지 확인하시고 다시 시도해 주세요\n\n"
                        + "다시 시도해도 정보를 찾지 못할 경우 서비스 지역이 아닐 수 있어요";
                return new Message(message);*/
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
