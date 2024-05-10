package com.example.countryinfo.controller;

import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Country;
import com.example.countryinfo.service.CounterService;
import com.example.countryinfo.service.CountryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/countryinfo/country")
public class CountryController {

  private final CountryService countryService;
  private final CounterService requestCounter;

  @GetMapping("/all")
  public List<Country> getAllCountries() {
    requestCounter.increment();
    return countryService.getAllCountries();
  }

  @GetMapping("/{id}")
  public Country getCountryById(@PathVariable Integer id) throws ObjectNotFoundException {
    requestCounter.increment();
    return countryService.getCountryById(id);
  }

  @PostMapping("/create")
  public void createCountry(@RequestBody Country country) throws ObjectExistException {
    requestCounter.increment();
    countryService.createCountry(country);
  }

  @PutMapping("/update/{id}")
  public void updateCountry(
      @PathVariable Integer id, @RequestParam String name, @RequestParam String beerSupply)
      throws BadRequestException {
    requestCounter.increment();
    countryService.updateCountry(id, name, beerSupply);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteCountry(@PathVariable Integer id) throws BadRequestException {
    countryService.deleteCountry(id);
    requestCounter.increment();
  }

  @PostMapping("/addLang/{countryName}/{languageId}")
  public Country addLanguageByIdCountry(
      @PathVariable String countryName, @PathVariable Integer languageId)
      throws BadRequestException {
    requestCounter.increment();
    return countryService.addLanguageInCountryById(countryName, languageId);
  }

  @PostMapping("/addCurr/{countryName}/{currencyId}")
  public Country addCurrencyByIdCountry(
      @PathVariable String countryName, @PathVariable Integer currencyId)
      throws BadRequestException {
    requestCounter.increment();
    return countryService.addCurrencyInCountryById(countryName, currencyId);
  }

  @GetMapping("/getByName/{countryName}")
  public Country getCountryByName(@PathVariable String countryName) {
    requestCounter.increment();
    return countryService.getCountryByName(countryName);
  }
}