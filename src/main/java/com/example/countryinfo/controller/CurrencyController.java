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

import com.example.countryinfo.model.Currency;
import com.example.countryinfo.service.CurrencyService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/countryinfo/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/all")
    public List<Currency> getAllCurrency() {
        return currencyService.getAllCurrency();
    }

    @GetMapping("/{id}")
    public Currency getCurrency(@PathVariable Long id) {
        return currencyService.getCurrencyById(id);
    }

    @PostMapping("/create")
    public void createCurrency(@RequestBody Currency currency) {
        currencyService.createCurrency(currency);
    }

    @PutMapping("/update/{id}")
    public void updateCurrency(@PathVariable Long id, @RequestParam String name, @RequestParam Long usdPrice) {
        currencyService.updateCurrency(id, name, usdPrice);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
    }
}
