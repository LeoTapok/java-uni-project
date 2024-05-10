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
import com.example.countryinfo.model.Currency;
import com.example.countryinfo.repository.CurrencyRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CurrencyServiceTest {

  @InjectMocks private CurrencyService currencyService;

  @Mock private CurrencyRepository currencyRepository;

  @Mock private CacheService<Integer, Optional<Currency>> cacheService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllCurrency() {
    Currency currency1 = new Currency();
    currency1.setId(1);
    Currency currency2 = new Currency();
    currency2.setId(2);

    when(currencyRepository.findAll()).thenReturn(Arrays.asList(currency1, currency2));

    List<Currency> result = currencyService.getAllCurrency();

    verify(cacheService, times(1)).put(anyInt(), any());
    assertEquals(2, result.size());
    assertEquals(Integer.valueOf(1), result.get(0).getId());
    assertEquals(Integer.valueOf(2), result.get(1).getId());
  }

  @Test
  void testGetCurrencyById() {
    Integer id = 1;
    Currency currency = new Currency();
    currency.setId(id);

    when(currencyRepository.findById(anyInt())).thenReturn(Optional.of(currency));

    Currency result = null;
    try {
      result = currencyService.getCurrencyById(id);
    } catch (ObjectNotFoundException e) {
      System.out.println("Currency not found with id: " + id);
    }
    verify(cacheService, times(1)).put(anyInt(), any());
    assertEquals(id, result.getId());
  }

  @Test
  void testGetCurrencyByIdThrowsObjectNotFoundException() {
    Integer id = 1;

    when(currencyRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(ObjectNotFoundException.class, () -> currencyService.getCurrencyById(id));
  }

  @Test
  void testCreateCurrency() throws ObjectExistException {
    String currencyName = "CurrencyName";
    Currency currency = new Currency();
    currency.setName(currencyName);

    when(currencyRepository.findByName(anyString())).thenReturn(Optional.empty());

    currencyService.createCurrency(currency);

    verify(currencyRepository, times(1)).save(any(Currency.class));
  }

  @Test
  void testUpdateCurrency() throws BadRequestException {
    Integer id = 1;
    String name = "CurrencyName";
    Float usdPrice = 1.0f;
    Currency currency = new Currency();
    currency.setId(id);
    currency.setName(name);
    currency.setUsdPrice(usdPrice);

    when(currencyRepository.findById(anyInt())).thenReturn(Optional.of(currency));

    currencyService.updateCurrency(id, name, usdPrice);

    verify(currencyRepository, times(1)).save(any(Currency.class));
  }

  @Test
  void testDeleteCurrency() throws BadRequestException {
    Integer id = 1;
    Currency currency = new Currency();
    currency.setId(id);

    when(currencyRepository.findById(anyInt())).thenReturn(Optional.of(currency));

    currencyService.deleteCurrency(id);

    verify(currencyRepository, times(1)).deleteById(anyInt());
  }

  @Test
  void testDeleteCurrencyThrowsBadRequestException() {
    Integer id = 1;
    when(currencyRepository.findById(id)).thenReturn(Optional.empty());
    assertThrows(BadRequestException.class, () -> currencyService.deleteCurrency(id));
  }
}