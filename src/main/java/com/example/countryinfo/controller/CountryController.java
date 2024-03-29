package com.example.countryinfo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.countryinfo.model.Country;
import com.example.countryinfo.service.CountryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/countryinfo/country")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<Country> getCountry(@RequestParam Long id) {
        return countryService.getCountry(id);
    }

    @GetMapping("/all")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    @PostMapping("/create")
    public void createCountry(@RequestBody Country country) {
        countryService.createCountry(country);
    }

    @PutMapping("/update/{id}")
    public void updateCountry(@PathVariable Long id, @RequestParam String name, @RequestParam String beerSupply) {
        countryService.updateCountry(id, name, beerSupply);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
    }
}