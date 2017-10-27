package com.capstonedesign.weathervessel.domain;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ObserveRepository extends CrudRepository<Observe, Integer> {
    List<Observe> findObserveByAddrIdOrderByTimeDesc(Address address_id);
    List<Observe> findObserveByDroneIdOrderByTimeDesc(Drone drone_id);
    List<Observe> findObserveByTimeGreaterThanEqual(LocalDateTime time);
    List<Observe> findObserveByDroneIdAndTimeGreaterThanEqualOrderByTimeDesc(Drone drone_id, LocalDateTime time);
}
