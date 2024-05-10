package com.example.countryinfo.controller;

import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Language;
import com.example.countryinfo.service.CounterService;
import com.example.countryinfo.service.LanguageService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/countryinfo/language")
public class LanguageController {
  private final LanguageService languageService;
  private final CounterService requestCounter;

  @GetMapping("/all")
  public List<Language> getAllLanguages() {
    requestCounter.increment();
    return languageService.getAllLanguages();
  }

  @GetMapping("/{id}")
  public Language getLanguageById(@PathVariable Integer id) throws ObjectNotFoundException {
    requestCounter.increment();
    return languageService.getLanguageById(id);
  }

  @PostMapping("/create")
  public void createLanguage(@RequestBody Language language) throws ObjectExistException {
    requestCounter.increment();
    languageService.createLanguage(language);
  }

  @PostMapping("/createmany")
  public void createLanguages(@RequestBody List<Language> languages) throws ObjectExistException {
    requestCounter.increment();
    languageService.createLanguages(languages);
  }

  @PutMapping("/update/{id}")
  public void updateLanguage(
      @PathVariable Integer id, @RequestParam String name, @RequestParam Long speakers)
      throws BadRequestException {
    requestCounter.increment();
    languageService.updateLanguage(id, name, speakers);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteLanguage(@PathVariable Integer id) throws BadRequestException {
    requestCounter.increment();
    languageService.deleteLanguage(id);
  }
}
