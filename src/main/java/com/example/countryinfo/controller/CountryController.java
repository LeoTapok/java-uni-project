package com.example.countryinfo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.countryinfo.model.Country;
import com.example.countryinfo.service.CountryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("papi/v1/countryinfo")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<Country> getCountry(@RequestParam Long id) {
        return countryService.getCountry(id);
    }
}