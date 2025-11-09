package com.sqlstar.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public class IngestRequest {

    @NotBlank
    private String query;

    private Map<String, Object> metadata;

    public IngestRequest() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
