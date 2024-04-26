package com.example.countryinfo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.countryinfo.cache.CacheService;
import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Language;
import com.example.countryinfo.repository.LanguageRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import static com.example.countryinfo.utilities.Constants.NOT_FOUND_MSG;
import static com.example.countryinfo.utilities.Constants.BAD_REQUEST_MSG;
import static com.example.countryinfo.utilities.Constants.OBJECT_EXIST_MSG;

@Service
@AllArgsConstructor
public class LanguageService {

    private LanguageRepository languageRepository;

    private static final Integer ALL_CONTAINS = 69420;

    private CacheService<Integer, Optional<Language>> cacheService;

    private void updateCacheService() {
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

    @Transactional
    public void updateLanguage(Integer id, String name, Long speakers) throws BadRequestException {
        Optional<Language> language;
        Integer hash = Objects.hashCode(id);
        if (cacheService.containsKey(hash)) {
            language = cacheService.get(hash);
        } else {
            language = languageRepository.findById(id);
        }
        if (language.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        language.get().setName(name);
        language.get().setSpeakers(speakers);
        if (cacheService.containsKey(id)) {
            cacheService.remove(id);
        }
        cacheService.put(id, language);
        languageRepository.save(language.get());
    }

    public void deleteLanguage(Integer id) throws BadRequestException {
        Optional<Language> language;
        Integer hash = Objects.hash(id);
        if (cacheService.containsKey(hash)) {
            language = cacheService.get(hash);
        } else {
            language = languageRepository.findById(id);
        }
        if (language.isEmpty()) {
            throw new BadRequestException(BAD_REQUEST_MSG);
        }
        if (cacheService.containsKey(hash)) {
            cacheService.remove(hash);
        }
        languageRepository.deleteById(id);
    }
}
