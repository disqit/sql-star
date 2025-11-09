package com.sqlstar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqlstar.dto.SeedResult;
import com.sqlstar.model.FilteredWord;
import com.sqlstar.repository.FilteredWordRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilteredWordService {

    private final FilteredWordRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FilteredWordService(FilteredWordRepository repository) {
        this.repository = repository;
    }

    // CRUD operations

    public List<FilteredWord> listAll() {
        return repository.findAll();
    }

    public FilteredWord get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filtered word not found"));
    }

    public FilteredWord create(String word) {
        String normalized = normalize(word);
        if (normalized.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "word must not be blank");
        }
        if (repository.existsByWordIgnoreCase(normalized)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "word already exists");
        }
        return repository.save(new FilteredWord(normalized));
    }

    public FilteredWord update(UUID id, String word) {
        String normalized = normalize(word);
        if (normalized.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "word must not be blank");
        }
        FilteredWord existing = get(id);

        Optional<FilteredWord> byWord = repository.findByWordIgnoreCase(normalized);
        if (byWord.isPresent() && !byWord.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "another record with this word already exists");
        }

        existing.setWord(normalized);
        return repository.save(existing);
    }

    public void delete(UUID id) {
        FilteredWord existing = get(id);
        repository.delete(existing);
    }

    // Seeding from classpath resource

    public SeedResult seedFromResource(boolean onlyIfEmpty) {
        if (onlyIfEmpty && repository.count() > 0) {
            return new SeedResult(0, 0);
        }
        List<String> words = readWordsFromClasspath();
        int inserted = 0;
        int skipped = 0;

        for (String w : words) {
            String normalized = normalize(w);
            if (normalized.isEmpty()) {
                continue;
            }
            if (repository.existsByWordIgnoreCase(normalized)) {
                skipped++;
            } else {
                repository.save(new FilteredWord(normalized));
                inserted++;
            }
        }
        return new SeedResult(inserted, skipped);
    }

    private List<String> readWordsFromClasspath() {
        try (InputStream is = new ClassPathResource("sql_sensitive_list.txt").getInputStream()) {
            return objectMapper.readValue(is, new TypeReference<List<String>>(){});
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read sql_sensitive_list.txt", e);
        }
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim();
    }
}
