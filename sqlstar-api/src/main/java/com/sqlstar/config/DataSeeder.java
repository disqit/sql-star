package com.sqlstar.config;

import com.sqlstar.dto.SeedResult;
import com.sqlstar.service.FilteredWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final FilteredWordService filteredWordService;

    public DataSeeder(FilteredWordService filteredWordService) {
        this.filteredWordService = filteredWordService;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Seed only if table is empty, to avoid overriding user changes.
        SeedResult result = filteredWordService.seedFromResource(true);
        if (result.getInserted() > 0 || result.getSkipped() > 0) {
            log.info("Sensitive words seed result: inserted={}, skipped={}", result.getInserted(), result.getSkipped());
        }
    }
}
