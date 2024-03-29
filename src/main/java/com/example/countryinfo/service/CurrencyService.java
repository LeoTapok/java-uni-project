package com.example.countryinfo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.countryinfo.model.Currency;
import com.example.countryinfo.repository.CurrencyRepository;

import jakarta.transaction.Transactional;

@Service
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    public Currency getCurrencyById(Long id) {
        return currencyRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "currency with id " + id + "does not exist"));
    }

    public List<Currency> getAllCurrency() {
        return currencyRepository.findAll();
    }

    public void createCurrency(Currency currency) {
        Optional<Currency> currencyOptional = currencyRepository
                .findByName(currency.getName());
        if (currencyOptional.isPresent()) {
            throw new IllegalStateException("currency exists");
        }
        currencyRepository.save(currency);
    }

    @Transactional
    public void updateCurrency(Long id, String name, Long usdPrice) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "currency with id " + id + "can not be updated, because it does not exist"));

        if (name != null && !name.isEmpty() && !Objects.equals(currency.getName(), name)) {
            Optional<Currency> currencyOptional = currencyRepository.findByName(name);
            if (currencyOptional.isPresent()) {
                throw new IllegalStateException("country with this name exists");
            }
            currency.setName(name);
        }

        if (usdPrice != null && usdPrice > 0 && !Objects.equals(currency.getUsdPrice(), usdPrice)) {
            currency.setUsdPrice(usdPrice);
        }
    }

    public void deleteCurrency(Long id) {
        boolean exists = currencyRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException(
                    "currency, which id " + id + " can not be deleted, because id does not exist");
        }
        currencyRepository.deleteById(id);
    }
}
