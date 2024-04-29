package com.example.countryinfo.repository;

import com.example.countryinfo.model.Currency;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
  @Query("SELECT s FROM Currency s WHERE s.name = :name")
  Optional<Currency> findByName(@Param("name") String name);
}