package com.example.countryinfo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.countryinfo.model.Country;

@Service

public class CountryService {

    public List<Country> getCountry(Long id) {
        return List.of(
                Country.builder().name("StranaPiwka" + id).build());
    }
}
