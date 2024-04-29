package com.example.countryinfo.model;

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
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "countries")
public class Country {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "beer_supply") // happines rating
  private String beerSupply;

  @ManyToOne
  @JoinColumn(name = "currency_id")
  private Currency currency;

  @ManyToMany
  @JoinTable(
      name = "country_language",
      joinColumns = @JoinColumn(name = "language_id"),
      inverseJoinColumns = @JoinColumn(name = "country_id"))
  private List<Language> languages;
}
