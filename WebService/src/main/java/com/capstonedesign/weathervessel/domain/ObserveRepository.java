package com.capstonedesign.weathervessel.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObserveRepository extends CrudRepository<Observe, Integer> {
    List<Observe> findObserveByAddrIdOrderByTimeDesc(Address address_id);
}
