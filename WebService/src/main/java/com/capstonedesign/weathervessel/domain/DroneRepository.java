package com.capstonedesign.weathervessel.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DroneRepository extends CrudRepository<Drone, Integer> {
    List<Drone> getAllBy();
    Drone findByDroneId(Integer id);
}
