package com.capstonedesign.weathervessel.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface QuestionsRepository extends CrudRepository<Questions, Integer> {
    List<Questions> findByIdGreaterThan(Integer i);
    List<Questions> findAll();
}
