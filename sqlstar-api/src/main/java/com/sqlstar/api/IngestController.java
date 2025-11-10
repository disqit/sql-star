package com.sqlstar.api;

import com.sqlstar.dto.IngestRequest;
import com.sqlstar.dto.IngestResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.sqlstar.repository.FilteredWordRepository;
import com.sqlstar.service.MessageFilterService;

@RestController
@Tag(name = "Ingest", description = "Ingest preview endpoint for internal validation")
@RequestMapping("/sqlstar")
public class IngestController {

    private final FilteredWordRepository filteredWordRepository;
    private final MessageFilterService messageFilterService;

    public IngestController(FilteredWordRepository filteredWordRepository, MessageFilterService messageFilterService) {
        this.filteredWordRepository = filteredWordRepository;
        this.messageFilterService = messageFilterService;
    }

    @Operation(
        summary = "Ingest SQL-like request",
        description = "Validates input and returns a preview. Touches DB by counting filtered words."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ingested successfully",
                content = @Content(schema = @Schema(implementation = IngestResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping({ "", "/" })
    public ResponseEntity<IngestResponse> ingestQuery(@Valid @RequestBody IngestRequest request) {
        String query = request.getQuery();
        // Example DB call: count rows in filteredWords table (ensures DB is touched)
        // Future: can cache
        long filteredWordsCount = filteredWordRepository.count();
        System.out.println("filteredWords rows count = " + filteredWordsCount);
        String preview = query == null ? "" : (query.length() > 100 ? query.substring(0, 100) + "..." : query);
        String ts = java.time.OffsetDateTime.now().toString();
        MessageFilterService.Result result = messageFilterService.censor(query);
        IngestResponse resp = new IngestResponse("ok", ts, preview, result.amendedText(), result.replacements());
        return ResponseEntity.ok(resp);
    }
}
