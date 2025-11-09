package com.sqlstar.dto;

public class IngestResponse {
    private String status;
    private String receivedAt;
    private String queryPreview;

    public IngestResponse() {
    }

    public IngestResponse(String status, String receivedAt, String queryPreview) {
        this.status = status;
        this.receivedAt = receivedAt;
        this.queryPreview = queryPreview;
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
}
