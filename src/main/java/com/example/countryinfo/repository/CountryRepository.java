package com.example.countryinfo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.countryinfo.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    @Query("SELECT s FROM Country s WHERE s.name = :name")
    Optional<Country> findByName(@Param("name") String name);
}
