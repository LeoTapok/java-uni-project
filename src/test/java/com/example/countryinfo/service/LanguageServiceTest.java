package com.example.countryinfo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.countryinfo.cache.CacheService;
import com.example.countryinfo.exception.BadRequestException;
import com.example.countryinfo.exception.ObjectExistException;
import com.example.countryinfo.exception.ObjectNotFoundException;
import com.example.countryinfo.model.Language;
import com.example.countryinfo.repository.LanguageRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LanguageServiceTest {

  @InjectMocks private LanguageService languageService;

  @Mock private LanguageRepository languageRepository;

  @Mock private CacheService<Integer, Optional<Language>> cacheService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetLanguageById() {
    Integer id = 1;
    Language language = new Language();
    language.setId(id);
    when(cacheService.containsKey(anyInt())).thenReturn(false);
    when(languageRepository.findById(id)).thenReturn(Optional.of(language));
    Language result = null;
    try {
      result = languageService.getLanguageById(id);
    } catch (ObjectNotFoundException e) {
      System.out.println("Language not found with id: " + id);
    }
    verify(cacheService, times(1)).put(anyInt(), any());
    assertSame(language, result);
  }

  @Test
  void testGetLanguageByIdNotFound() {
    Integer id = 1;
    when(cacheService.containsKey(anyInt())).thenReturn(false);
    when(languageRepository.findById(id)).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class, () -> languageService.getLanguageById(id));
  }

  @Test
  void testGetAllLanguages() {
    Language language1 = new Language();
    language1.setId(1);
    Language language2 = new Language();
    language2.setId(2);
    when(languageRepository.findAll()).thenReturn(Arrays.asList(language1, language2));
    List<Language> result = languageService.getAllLanguages();
    verify(cacheService, times(1)).put(anyInt(), any());
    assertEquals(2, result.size());
    assertEquals(Integer.valueOf(1), result.get(0).getId());
    assertEquals(Integer.valueOf(2), result.get(1).getId());
  }

  @Test
  void testCreateLanguage() {
    Language language = new Language();
    language.setName("English");
    language.setSpeakers(1000000L);
    when(languageRepository.findByName(anyString())).thenReturn(Optional.empty());
    assertNotNull(language);
    try {
      languageService.createLanguage(language);
    } catch (Exception e) {
      System.out.println("null");
    }
    verify(languageRepository, times(1)).save(any(Language.class));
  }

  @Test
  void testCreateLanguageThrowsObjectExistException() {
    Language language = new Language();
    language.setName("English");
    language.setSpeakers(1000000L);
    when(languageRepository.findByName(anyString())).thenReturn(Optional.of(language));
    assertThrows(ObjectExistException.class, () -> languageService.createLanguage(language));
  }

  @Test
  void testDeleteLanguage() {
    Integer id = 1;
    Language language = new Language();
    language.setId(id);
    when(languageRepository.findById(anyInt())).thenReturn(Optional.of(language));
    try {
      languageService.deleteLanguage(id);
    } catch (Exception e) {
      System.out.println("null");
    }
    verify(languageRepository, times(1)).deleteById(anyInt());
  }

  @Test
  void testDeleteLanguageThrowsBadRequestException() {
    Integer id = 1;
    when(languageRepository.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(BadRequestException.class, () -> languageService.deleteLanguage(id));
  }

  @Test
  void testUpdateLanguage() {
    Integer id = 1;
    String name = "English";
    Long speakers = 1000000L;
    Language language = new Language();
    language.setId(id);
    language.setName(name);
    language.setSpeakers(speakers);
    when(languageRepository.findById(anyInt())).thenReturn(Optional.of(language));
    try {
      languageService.updateLanguage(id, name, speakers);
    } catch (Exception e) {
      System.out.println("null");
    }
    verify(languageRepository, times(1)).save(any(Language.class));
  }

  @Test
  void testUpdateLanguageThrowsBadRequestException() {
    Integer id = 1;
    String name = "English";
    Long speakers = 1000000L;
    when(languageRepository.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(
        BadRequestException.class, () -> languageService.updateLanguage(id, name, speakers));
  }

  @Test
  void testCreateLanguagesSuccess() throws ObjectExistException {
    Language language1 = new Language();
    language1.setName("Русский");
    language1.setSpeakers(150000000L);
    Language language2 = new Language();
    language2.setName("English");
    language2.setSpeakers(1348000000L);
    List<Language> languages = List.of(language1, language2);
    languageService.createLanguages(languages);
    verify(languageRepository, times(1)).save(language1);
    verify(languageRepository, times(1)).save(language2);
  }

  @Test
  void testCreateLanguagesEmptyList() throws ObjectExistException {
    List<Language> languages = List.of();
    languageService.createLanguages(languages);
    verify(languageRepository, never()).save(any(Language.class));
  }
}
