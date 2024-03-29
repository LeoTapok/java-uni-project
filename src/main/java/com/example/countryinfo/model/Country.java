package com.example.countryinfo.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "beer_supply") // happines rating
    private String beerSupply;

    @ManyToOne
    @Column
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToMany
    @JoinTable(name = "country_currency", joinColumns = @JoinColumn(name = "language_id"), inverseJoinColumns = @JoinColumn(name = "country_id"))
    private Set<Language> languages;
}
