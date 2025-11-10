package com.sqlstar.dto;

public class IngestResponse {
    private String status;
    private String receivedAt;
    private String queryPreview;
    private String amendedText;
    private int replacements;

    public IngestResponse() {
    }

    public IngestResponse(String status, String receivedAt, String queryPreview) {
        this.status = status;
        this.receivedAt = receivedAt;
        this.queryPreview = queryPreview;
    }

    public IngestResponse(String status, String receivedAt, String queryPreview, String amendedText, int replacements) {
        this.status = status;
        this.receivedAt = receivedAt;
        this.queryPreview = queryPreview;
        this.amendedText = amendedText;
        this.replacements = replacements;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getQueryPreview() {
        return queryPreview;
    }

    public void setQueryPreview(String queryPreview) {
        this.queryPreview = queryPreview;
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
