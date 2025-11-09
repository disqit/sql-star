package com.sqlstar.repository;

import com.sqlstar.model.FilteredWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FilteredWordRepository extends JpaRepository<FilteredWord, UUID> {
    Optional<FilteredWord> findByWordIgnoreCase(String word);
    boolean existsByWordIgnoreCase(String word);
}
