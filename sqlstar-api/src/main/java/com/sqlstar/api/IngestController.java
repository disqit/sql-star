package com.sqlstar.api;

import com.sqlstar.dto.IngestRequest;
import com.sqlstar.dto.IngestResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sqlstar.repository.FilteredWordRepository;

@RestController
@RequestMapping("/sqlstar")
public class IngestController {

    private final FilteredWordRepository filteredWordRepository;

    public IngestController(FilteredWordRepository filteredWordRepository) {
        this.filteredWordRepository = filteredWordRepository;
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<IngestResponse> ingestQuery(@Valid @RequestBody IngestRequest request) {
        String query = request.getQuery();
        // Example DB call: count rows in filteredWords table (ensures DB is touched)
        long filteredWordsCount = filteredWordRepository.count();
        System.out.println("filteredWords rows count = " + filteredWordsCount);
        String preview = query == null ? "" : (query.length() > 100 ? query.substring(0, 100) + "..." : query);
        String ts = java.time.OffsetDateTime.now().toString();
        IngestResponse resp = new IngestResponse("ok", ts, preview);
        return ResponseEntity.ok(resp);
    }
}
