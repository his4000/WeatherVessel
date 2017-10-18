package com.capstonedesign.weathervessel.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ObserveRepository extends CrudRepository<Observe, Integer> {
    List<Observe> findObserveByAddrIdOrderByTimeDesc(Address address_id);
    //@Query("select * from observe o1 where o1.id == (select o2.id from observe o2 where )")
}
