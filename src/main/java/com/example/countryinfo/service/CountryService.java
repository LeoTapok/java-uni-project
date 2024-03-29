package com.example.countryinfo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.countryinfo.model.Country;
import com.example.countryinfo.repository.CountryRepository;

import jakarta.transaction.Transactional;

@Service

public class CountryService {

    private CountryRepository countryRepository;

    public List<Country> getCountry(Long id) {
        return List.of(
                Country.builder().name("StranaPiwka" + id).build());
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryById(Long id) {
        return countryRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "country with id " + id + "does not exist"));
    }

    public void createCountry(Country country) {
        Optional<Country> countryOptional = countryRepository
                .findByName(country.getName());
        if (countryOptional.isPresent()) {
            throw new IllegalStateException("country exists");
        }
        countryRepository.save(country);
    }

    @Transactional
    public void updateCountry(Long id, // may be add some other fields
            String name,
            String beerSupply) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "country with id " + id + "can not be updated, because it does not exist"));

        if (name != null && !name.isEmpty() && !Objects.equals(country.getName(), name)) {
            Optional<Country> countryOptional = countryRepository.findByName(name);
            if (countryOptional.isPresent()) {
                throw new IllegalStateException("country with this name exists");
            }
            country.setName(name);
        }

        if (beerSupply != null && !beerSupply.isEmpty() && !Objects.equals(country.getBeerSupply(), beerSupply)) {
            country.setBeerSupply(beerSupply);
        }
    }

    public void deleteCountry(Long id) {
        boolean exists = countryRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException(
                    "country, which id " + id + " can not be deleted, because id does not exist");
        }
        countryRepository.deleteById(id);
    }
}
