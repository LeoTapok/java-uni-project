package com.example.countryinfo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CountryServiceTest {

  @InjectMocks private CountryService countryService;

  @Mock private CountryRepository countryRepository;

  @Mock private LanguageRepository languageRepository;

  @Mock private CurrencyRepository currencyRepository;

  @Mock private CacheService<Integer, Optional<Country>> cacheService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllCountries() {
    Country country1 = new Country();
    country1.setId(1);
    Country country2 = new Country();
    country2.setId(2);

    when(countryRepository.findAll()).thenReturn(Arrays.asList(country1, country2));

    List<Country> result = countryService.getAllCountries();

    verify(cacheService, times(1)).put(anyInt(), any());
    assertEquals(2, result.size());
    assertEquals(Integer.valueOf(1), result.get(0).getId());
    assertEquals(Integer.valueOf(2), result.get(1).getId());
  }

  @Test
  void testGetCountryById() {
    Integer id = 1;
    Country country = new Country();
    country.setId(id);

    when(countryRepository.findById(anyInt())).thenReturn(Optional.of(country));

    Country result = null;
    try {
      result = countryService.getCountryById(id);
    } catch (ObjectNotFoundException e) {
      System.out.println("Country not found with id: " + id);
    }

    verify(cacheService, times(1)).put(anyInt(), any());
    assertEquals(id, result.getId());
  }

  @Test
  void testGetCountryByIdThrowsObjectNotFoundException() {
    Integer id = 1;

    when(countryRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(ObjectNotFoundException.class, () -> countryService.getCountryById(id));
  }

  @Test
  void testAddCurrencyInCountryById() throws BadRequestException {
    String countryName = "CountryName";
    Integer currencyId = 1;
    Country country = new Country();
    country.setName(countryName);
    Currency currency = new Currency();
    currency.setId(currencyId);

    when(countryRepository.findByName(anyString())).thenReturn(Optional.of(country));
    when(currencyRepository.findById(anyInt())).thenReturn(Optional.of(currency));

    countryService.addCurrencyInCountryById(countryName, currencyId);

    verify(countryRepository, times(1)).save(any(Country.class));
  }

  @Test
  void testCreateCountry() throws ObjectExistException {
    String countryName = "CountryName";
    Country country = new Country();
    country.setName(countryName);

    when(countryRepository.findByName(anyString())).thenReturn(Optional.empty());

    countryService.createCountry(country);

    verify(countryRepository, times(1)).save(any(Country.class));
  }

  @Test
  void testDeleteCountry() throws BadRequestException {
    Integer id = 1;
    Country country = new Country();
    country.setId(id);

    when(countryRepository.findById(anyInt())).thenReturn(Optional.of(country));

    countryService.deleteCountry(id);

    verify(countryRepository, times(1)).deleteById(anyInt());
  }

  @Test
  void testUpdateCountry() throws BadRequestException {
    Integer id = 1;
    String name = "CountryName";
    String beerSupply = "High";
    Country country = new Country();
    country.setId(id);
    country.setName(name);
    country.setBeerSupply(beerSupply);

    when(countryRepository.findById(anyInt())).thenReturn(Optional.of(country));

    countryService.updateCountry(id, name, beerSupply);

    verify(countryRepository, times(1)).save(any(Country.class));
  }

  @Test
  void testAddLanguageInCountryByIdSuccess() throws BadRequestException {
    String countryName = "Russia";
    Integer languageId = 1;
    Country country = new Country();
    country.setId(1);
    country.setName(countryName);
    country.setLanguages(new ArrayList<>());
    Language language = new Language();
    language.setId(languageId);
    when(countryRepository.findByName(countryName)).thenReturn(Optional.of(country));
    when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
    when(countryRepository.save(country)).thenReturn(country);
    Country result = countryService.addLanguageInCountryById(countryName, languageId);
    assertEquals(1, result.getLanguages().size());
    assertEquals(language, result.getLanguages().get(0));
    verify(countryRepository, times(1)).save(country);
  }

  @Test
  void testAddLanguageInCountryByIdCountryNotFound() {
    String countryName = "Nonexistent Country";
    Integer languageId = 1;
    when(countryRepository.findByName(countryName)).thenReturn(Optional.empty());
    assertThrows(
        BadRequestException.class,
        () -> countryService.addLanguageInCountryById(countryName, languageId));
  }

  @Test
  void testAddLanguageInCountryByIdLanguageNotFound() {
    String countryName = "Russia";
    Integer languageId = 1;
    Country country = new Country();
    country.setId(1);
    country.setName(countryName);
    country.setLanguages(new ArrayList<>());
    when(countryRepository.findByName(countryName)).thenReturn(Optional.of(country));
    when(languageRepository.findById(languageId)).thenReturn(Optional.empty());
    assertThrows(
        BadRequestException.class,
        () -> countryService.addLanguageInCountryById(countryName, languageId));
  }

  @Test
  void testGetCountryByNameFound() {
    String countryName = "Poland";
    Country expectedCountry = new Country();
    expectedCountry.setName(countryName);
    when(countryRepository.findByName(countryName)).thenReturn(Optional.of(expectedCountry));

    Country actualCountry = countryService.getCountryByName(countryName);
    assertEquals(expectedCountry, actualCountry);
  }
}
