package com.capstonedesign.kakaobot.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface QuestionsRepository extends CrudRepository<Questions, Integer> {
    Questions findById(Integer i);
    Long countAllBy();
}
