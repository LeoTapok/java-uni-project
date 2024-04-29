package com.example.countryinfo.repository;

import com.example.countryinfo.model.Language;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
  @Query("SELECT s FROM Language s WHERE s.name = :name")
  Optional<Language> findByName(@Param("name") String name);
}