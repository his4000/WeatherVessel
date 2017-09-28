package com.capstonedesign.kakaobot.domain;

import org.springframework.data.repository.CrudRepository;

public interface AddressIndexRepository extends CrudRepository<AddressIndex, Integer> {
    AddressIndex findByAddress(String address);
}
