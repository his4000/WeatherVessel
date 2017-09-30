package com.capstonedesign.kakaobot.domain;

import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Integer> {
    Address findByAddress(String address);
}
