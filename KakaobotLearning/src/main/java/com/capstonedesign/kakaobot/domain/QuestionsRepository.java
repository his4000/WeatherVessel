package com.capstonedesign.kakaobot.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionsRepository extends CrudRepository<Questions, Integer> {
    List<Questions> findByIdGreaterThan(Integer i);
}
