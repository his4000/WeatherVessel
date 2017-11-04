package com.capstonedesign.weathervessel.service.messaging.monitor;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.messaging.Message;
import com.capstonedesign.weathervessel.service.messaging.Reply;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;

public class MonitorReply implements Reply {

    public Message getReplyMessage(String content, NaturalLanguageProcessing naturalLanguageProcessing, ObserveRepository observeRepository, AddressRepository addressRepository){
        return new Message("monitor");
    }
}
