package com.sqlstar.dto;

import java.util.UUID;

public class FilteredWordResponse {

    private UUID id;
    private String word;

    public FilteredWordResponse() {
    }

    public FilteredWordResponse(UUID id, String word) {
        this.id = id;
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
