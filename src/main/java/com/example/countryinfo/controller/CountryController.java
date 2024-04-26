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

import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Country;
import com.example.countryinfo.service.CountryService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/countryinfo/country")
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/all")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Integer id) throws ObjectNotFoundException {
        return countryService.getCountryById(id);
    }

    @PostMapping("/create")
    public void createCountry(@RequestBody Country country) throws ObjectExistException {
        countryService.createCountry(country);
    }

    @PutMapping("/update/{id}")
    public void updateCountry(@PathVariable Integer id, @RequestParam String name, @RequestParam String beerSupply)
            throws BadRequestException {
        countryService.updateCountry(id, name, beerSupply);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCountry(@PathVariable Integer id) throws BadRequestException {
        countryService.deleteCountry(id);
    }

    @PostMapping("/addLang/{countryName}/{languageId}")
    public Country addLanguageByIdCountry(@PathVariable String countryName, @PathVariable Integer languageId)
            throws BadRequestException {
        return countryService.addLanguageInCountryById(countryName, languageId);
    }

    @PostMapping("/addCurr/{countryName}/{currencyId}")
    public Country addCurrencyByIdCountry(@PathVariable String countryName, @PathVariable Integer currencyId)
            throws BadRequestException {
        return countryService.addCurrencyInCountryById(countryName, currencyId);
    }
}