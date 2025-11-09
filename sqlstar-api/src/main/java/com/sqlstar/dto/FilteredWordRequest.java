package com.sqlstar.dto;

import jakarta.validation.constraints.NotBlank;

public class FilteredWordRequest {

    @NotBlank
    private String word;

    public FilteredWordRequest() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
