package com.example.countryinfo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.countryinfo.model.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    @Query("SELECT s FROM languages s WHERE s.name:=name")
    Optional<Language> findByName(@Param("name") String name);

}
