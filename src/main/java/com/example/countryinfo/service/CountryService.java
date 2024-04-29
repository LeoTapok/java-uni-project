package com.example.countryinfo.service;

import static com.example.countryinfo.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.countryinfo.utilities.Constants.NOT_FOUND_MSG;
import static com.example.countryinfo.utilities.Constants.OBJECT_EXIST_MSG;

import com.example.countryinfo.aop.annotation.Logging;
import com.example.countryinfo.cache.CacheService;
import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Country;
import com.example.countryinfo.model.Currency;
import com.example.countryinfo.model.Language;
import com.example.countryinfo.repository.CountryRepository;
import com.example.countryinfo.repository.CurrencyRepository;
import com.example.countryinfo.repository.LanguageRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Logging
@Service
@AllArgsConstructor
public class CountryService {

  private CountryRepository countryRepository;
  private LanguageRepository languageRepository;
  private CurrencyRepository currencyRepository;

  private static final Integer ALL_CONTAINS = 69420;

  private CacheService<Integer, Optional<Country>> cacheService;

  private void updateCacheService() {
    if (!cacheService.containsKey(ALL_CONTAINS)) {
      List<Country> countries = countryRepository.findAll();
      for (Country country : countries) {
        if (cacheService.containsKey(country.getId())) {
          Integer hash = Objects.hash(country.getId());
          cacheService.put(hash, Optional.of(country));
        }
      }
      cacheService.put(ALL_CONTAINS, null);
    }
  }

  public List<Country> getAllCountries() {
    updateCacheService();
    return countryRepository.findAll();
  }

  public Country getCountryById(Integer id) throws ObjectNotFoundException {
    Integer hash = Objects.hashCode(id);
    Optional<Country> country;
    if (cacheService.containsKey(hash)) {
      country = cacheService.get(hash);
    } else {
      country = countryRepository.findById(id);
      cacheService.put(hash, country);
    }
    if (country.isEmpty()) {
      throw new ObjectNotFoundException(NOT_FOUND_MSG);
    }
    return country.get();
  }

  public void createCountry(Country country) throws ObjectExistException {
    Optional<Country> countryOptional = countryRepository.findByName(country.getName());
    if (countryOptional.isPresent()) {
      throw new ObjectExistException(OBJECT_EXIST_MSG);
    }
    countryRepository.save(country);
  }

  @Transactional
  public void updateCountry(Integer id, String name, String beerSupply) throws BadRequestException {
    Optional<Country> country;
    Integer hash = Objects.hashCode(id);
    if (cacheService.containsKey(hash)) {
      country = cacheService.get(hash);
    } else {
      country = countryRepository.findById(id);
    }
    if (country.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    country.get().setName(name);
    country.get().setBeerSupply(beerSupply);
    if (cacheService.containsKey(id)) {
      cacheService.remove(id);
    }
    cacheService.put(id, country);
    countryRepository.save(country.get());
  }

  public void deleteCountry(Integer id) throws BadRequestException {
    Optional<Country> country;
    Integer hash = Objects.hash(id);
    if (cacheService.containsKey(hash)) {
      country = cacheService.get(hash);
    } else {
      country = countryRepository.findById(id);
    }
    if (country.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    if (cacheService.containsKey(hash)) {
      cacheService.remove(hash);
    }
    countryRepository.deleteById(id);
  }

  @Transactional
  public Country addLanguageInCountryById(String countryName, Integer languageId)
      throws BadRequestException {
    Optional<Country> country = countryRepository.findByName(countryName);
    if (country.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }

    Optional<Language> language = languageRepository.findById(languageId);
    if (language.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    country.get().getLanguages().add(language.get());
    Integer hash = Objects.hashCode(country.get().getId());
    if (cacheService.containsKey(hash)) {
      cacheService.remove(hash);
    }
    cacheService.put(hash, country);
    return countryRepository.save(country.get());
  }

  @Transactional
  public Country addCurrencyInCountryById(String countryName, Integer currencyId)
      throws BadRequestException {
    Optional<Country> country = countryRepository.findByName(countryName);
    if (country.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    Optional<Currency> currency = currencyRepository.findById(currencyId);
    if (currency.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    country.get().setCurrency(currency.get());
    Integer hash = Objects.hashCode(country.get().getId());
    if (cacheService.containsKey(hash)) {
      cacheService.remove(hash);
    }
    cacheService.put(hash, country);
    return countryRepository.save(country.get());
  }

  public Country getCountryByName(String countryName) {
    return countryRepository
        .findByName(countryName)
        .orElseThrow(() -> new IllegalStateException("not found"));
  }
}
