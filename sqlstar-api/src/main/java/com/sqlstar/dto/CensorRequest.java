package com.sqlstar.dto;

import jakarta.validation.constraints.NotBlank;

public class CensorRequest {

    @NotBlank
    private String text;

    public CensorRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
