package com.capstonedesign.weathervessel.service.messaging;

import com.capstonedesign.weathervessel.domain.AddressRepository;
import com.capstonedesign.weathervessel.domain.DroneRepository;
import com.capstonedesign.weathervessel.domain.ObserveRepository;
import com.capstonedesign.weathervessel.service.natural_language_processing.NaturalLanguageProcessing;

public interface Reply {
    Message getReplyMessage(String content, NaturalLanguageProcessing naturalLanguageProcessing, ObserveRepository observeRepository, AddressRepository addressRepository, DroneRepository droneRepository);
}
