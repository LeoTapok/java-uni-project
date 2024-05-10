package com.example.countryinfo.service;

import static com.example.countryinfo.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.countryinfo.utilities.Constants.NOT_FOUND_MSG;
import static com.example.countryinfo.utilities.Constants.OBJECT_EXIST_MSG;

import com.example.countryinfo.aop.annotation.Logging;
import com.example.countryinfo.cache.CacheService;
import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Currency;
import com.example.countryinfo.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Logging
@Service
@AllArgsConstructor
public class CurrencyService {

  private final CurrencyRepository currencyRepository;
  private static final Integer ALL_CONTAINS = 69420;

  private final CacheService<Integer, Optional<Currency>> cacheService;

  private void updateCacheService() {
    if (!cacheService.containsKey(ALL_CONTAINS)) {
      List<Currency> currencies = currencyRepository.findAll();
      for (Currency currency : currencies) {
        if (cacheService.containsKey(currency.getId())) {
          Integer hash = Objects.hash(currency.getId());
          cacheService.put(hash, Optional.of(currency));
        }
      }
      cacheService.put(ALL_CONTAINS, null);
    }
  }

  public Currency getCurrencyById(Integer id) throws ObjectNotFoundException {
    Integer hash = Objects.hashCode(id);
    Optional<Currency> currency;
    if (cacheService.containsKey(hash)) {
      currency = cacheService.get(hash);
    } else {
      currency = currencyRepository.findById(id);
      cacheService.put(hash, currency);
    }
    if (currency.isEmpty()) {
      throw new ObjectNotFoundException(NOT_FOUND_MSG);
    }
    return currency.get();
  }

  public List<Currency> getAllCurrency() {
    updateCacheService();
    return currencyRepository.findAll();
  }

  public void createCurrency(Currency currency) throws ObjectExistException {
    Optional<Currency> currencyOptional = currencyRepository.findByName(currency.getName());
    if (currencyOptional.isPresent()) {
      throw new ObjectExistException(OBJECT_EXIST_MSG);
    }
    currencyRepository.save(currency);
  }

  @Transactional
  public void updateCurrency(Integer id, String name, Float usdPrice) throws BadRequestException {
    Optional<Currency> currency;
    currency = currencyRepository.findById(id);
    if (currency.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    currency.get().setName(name);
    currency.get().setUsdPrice(usdPrice);
    if (cacheService.containsKey(id)) {
      cacheService.remove(id);
    }
    currencyRepository.save(currency.get());
  }

  public void deleteCurrency(Integer id) throws BadRequestException {
    Optional<Currency> currency;
    currency = currencyRepository.findById(id);
    if (currency.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    currencyRepository.deleteById(id);
  }
}
