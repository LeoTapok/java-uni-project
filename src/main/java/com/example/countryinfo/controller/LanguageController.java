package com.example.countryinfo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.countryinfo.model.Language;
import com.example.countryinfo.service.LanguageService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/countryinfo/language")
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping("/all")
    public List<Language> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/{id}")
    public Language getLanguageById(@PathVariable Long id) {
        return languageService.getLanguageById(id);
    }

    @PostMapping("/create")
    public void createLaguage(@RequestBody Language language) {
        languageService.createLaguage(language);
    }

    @PutMapping("/update/{id}")
    public void updateLanguage(@PathVariable Long id, @RequestParam String name, @RequestParam Long speakers) {
        languageService.updateLanguage(id, name, speakers);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
    }
}
