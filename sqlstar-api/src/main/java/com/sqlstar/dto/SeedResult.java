package com.sqlstar.dto;

public class SeedResult {
    private int inserted;
    private int skipped;

    public SeedResult() {
    }

    public SeedResult(int inserted, int skipped) {
        this.inserted = inserted;
        this.skipped = skipped;
    }

    public int getInserted() {
        return inserted;
    }

    public void setInserted(int inserted) {
        this.inserted = inserted;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }
}
