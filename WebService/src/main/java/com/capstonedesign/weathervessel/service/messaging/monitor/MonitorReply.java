package com.capstonedesign.weathervessel.service.messaging.monitor;

import com.capstonedesign.weathervessel.domain.*;
import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.MessageButton;
import com.capstonedesign.weathervessel.service.messaging.Photo;
import com.capstonedesign.weathervessel.service.messaging.Reply;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;

public class MonitorReply implements Reply {

    private final String label = "드론 위치 보기";

    public Message getReplyMessage(String content, NaturalLanguageProcessing naturalLanguageProcessing, ObserveRepository observeRepository, AddressRepository addressRepository, DroneRepository droneRepository){
        Drone masterDrone = droneRepository.findByDroneId(1);
        Observe latestObserve = observeRepository.findObserveByDroneIdOrderByTimeDesc(masterDrone).get(0);
        String text = "현재 미세먼지 측정중인 드론은 Erle Robotics 사의 Erle-copter 입니다.\n\n"
                + "현재 드론의 위치는 " + latestObserve.getAddrId().toString() + "이며, \n\n"
                + "현재 드론의 GPS 좌표는"
                + "\n위도 : " + latestObserve.getGps_y()
                + "\n경도 : " + latestObserve.getGps_x()
                + "\n입니다.";

        return new Message(text
                , new MessageButton("http://ec2-13-124-179-202.ap-northeast-2.compute.amazonaws.com:8090/pointMonitoring/" + String.valueOf(latestObserve.getGps_y()) + "-" + String.valueOf(latestObserve.getGps_x()) + "-", label)
                , new Photo("http://ec2-13-124-179-202.ap-northeast-2.compute.amazonaws.com/Erlecopter.jpg"));
    }
}
