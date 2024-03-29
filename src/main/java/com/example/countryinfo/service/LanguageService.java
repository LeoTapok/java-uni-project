package com.example.countryinfo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.countryinfo.model.Language;
import com.example.countryinfo.repository.LanguageRepository;

import jakarta.transaction.Transactional;

@Service
public class LanguageService {

    private LanguageRepository languageRepository;

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Language getLanguageById(Long id) {
        return languageRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "language with id " + id + "does not exist"));
    }

    public void createLaguage(Language language) {
        Optional<Language> languageOptional = languageRepository
                .findByName(language.getName());
        if (languageOptional.isPresent()) {
            throw new IllegalStateException("language exists");
        }
        languageRepository.save(language);
    }

    @Transactional
    public void updateLanguage(Long id, String name, Long speakers) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "currency with id " + id + "can not be updated, because it does not exist"));

        if (name != null && !name.isEmpty() && !Objects.equals(language.getName(), name)) {
            Optional<Language> languageOptional = languageRepository.findByName(name);
            if (languageOptional.isPresent()) {
                throw new IllegalStateException("country with this name exists");
            }
            language.setName(name);
        }

        if (speakers != null && speakers > 0 && !Objects.equals(language.getSpeakers(), speakers)) {
            language.setSpeakers(speakers);
        }
    }

    public void deleteLanguage(Long id) {
        boolean exists = languageRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException(
                    "language, which id " + id + " can not be deleted, because id does not exist");
        }
        languageRepository.deleteById(id);
    }

}
