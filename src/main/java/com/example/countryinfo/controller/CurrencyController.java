package com.example.countryinfo.controller;

import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Currency;
import com.example.countryinfo.service.CounterService;
import com.example.countryinfo.service.CurrencyService;
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
@RequestMapping("api/v1/countryinfo/currency")
public class CurrencyController {
  private final CurrencyService currencyService;
  private final CounterService requestCounter;

  @GetMapping("/all")
  public List<Currency> getAllCurrency() {
    requestCounter.increment();
    return currencyService.getAllCurrency();
  }

  @GetMapping("/{id}")
  public Currency getCurrency(@PathVariable Integer id) throws ObjectNotFoundException {
    requestCounter.increment();
    return currencyService.getCurrencyById(id);
  }

  @PostMapping("/create")
  public void createCurrency(@RequestBody Currency currency) throws ObjectExistException {
    requestCounter.increment();
    currencyService.createCurrency(currency);
  }

  @PutMapping("/update/{id}")
  public void updateCurrency(
      @PathVariable Integer id, @RequestParam String name, @RequestParam Float usdPrice)
      throws BadRequestException {
    requestCounter.increment();
    currencyService.updateCurrency(id, name, usdPrice);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteCurrency(@PathVariable Integer id) throws BadRequestException {
    requestCounter.increment();
    currencyService.deleteCurrency(id);
  }
}
