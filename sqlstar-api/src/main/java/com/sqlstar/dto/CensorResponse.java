package com.sqlstar.dto;

public class CensorResponse {
    private String amendedText;
    private int replacements;

    public CensorResponse() {
    }

    public CensorResponse(String amendedText, int replacements) {
        this.amendedText = amendedText;
        this.replacements = replacements;
    }

    public String getAmendedText() {
        return amendedText;
    }

    public void setAmendedText(String amendedText) {
        this.amendedText = amendedText;
    }

    public int getReplacements() {
        return replacements;
    }

    public void setReplacements(int replacements) {
        this.replacements = replacements;
    }
}
