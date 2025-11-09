package com.sqlstar.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "filteredWords",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"word"})
        }
)
public class FilteredWord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "word", nullable = false, unique = true, length = 255)
    private String word;

    public FilteredWord() {
    }

    public FilteredWord(String word) {
        this.word = word;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
