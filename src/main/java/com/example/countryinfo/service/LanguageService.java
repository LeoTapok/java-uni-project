package com.example.countryinfo.service;

import static com.example.countryinfo.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.countryinfo.utilities.Constants.NOT_FOUND_MSG;
import static com.example.countryinfo.utilities.Constants.OBJECT_EXIST_MSG;

import com.example.countryinfo.aop.annotation.Logging;
import com.example.countryinfo.cache.CacheService;
import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Language;
import com.example.countryinfo.repository.LanguageRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Logging
@Service
@AllArgsConstructor
public class LanguageService {

  private final LanguageRepository languageRepository;

  private static final Integer ALL_CONTAINS = 69420;

  private final CacheService<Integer, Optional<Language>> cacheService;

  void updateCacheService() {
    if (!cacheService.containsKey(ALL_CONTAINS)) {
      List<Language> languages = languageRepository.findAll();
      for (Language language : languages) {
        if (cacheService.containsKey(language.getId())) {
          Integer hash = Objects.hash(language.getId());
          cacheService.put(hash, Optional.of(language));
        }
      }
      cacheService.put(ALL_CONTAINS, null);
    }
  }

  public List<Language> getAllLanguages() {
    updateCacheService();
    return languageRepository.findAll();
  }

  public Language getLanguageById(Integer id) throws ObjectNotFoundException {
    Integer hash = Objects.hashCode(id);
    Optional<Language> language;
    if (cacheService.containsKey(hash)) {
      language = cacheService.get(hash);
    } else {
      language = languageRepository.findById(id);
      cacheService.put(hash, language);
    }
    if (language.isEmpty()) {
      throw new ObjectNotFoundException(NOT_FOUND_MSG);
    }
    return language.get();
  }

  public void createLanguage(Language language) throws ObjectExistException {
    Optional<Language> languageOptional = languageRepository.findByName(language.getName());
    if (languageOptional.isPresent()) {
      throw new ObjectExistException(OBJECT_EXIST_MSG);
    }
    languageRepository.save(language);
  }

  public void createLanguages(List<Language> languages) throws ObjectExistException {
    for (Language language : languages) {
      Optional<Language> existingLanguage =
          languageRepository.findByName(language.getName()).stream().findFirst();
      if (existingLanguage.isPresent()) {
        throw new ObjectExistException(OBJECT_EXIST_MSG);
      }
      languageRepository.save(language);
    }
  }

  @Transactional
  public void updateLanguage(Integer id, String name, Long speakers) throws BadRequestException {
    Optional<Language> language;
    language = languageRepository.findById(id);
    if (language.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    language.get().setName(name);
    language.get().setSpeakers(speakers);
    if (cacheService.containsKey(id)) {
      cacheService.remove(id);
    }
    languageRepository.save(language.get());
  }

  public void deleteLanguage(Integer id) throws BadRequestException {
    Optional<Language> language;
    language = languageRepository.findById(id);
    if (language.isEmpty()) {
      throw new BadRequestException(BAD_REQUEST_MSG);
    }
    languageRepository.deleteById(id);
  }
}
