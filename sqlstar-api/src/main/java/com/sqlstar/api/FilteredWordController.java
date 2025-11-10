package com.sqlstar.api;

import com.sqlstar.dto.FilteredWordRequest;
import com.sqlstar.dto.FilteredWordResponse;
import com.sqlstar.dto.SeedResult;
import com.sqlstar.model.FilteredWord;
import com.sqlstar.service.FilteredWordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Filtered Words (Internal)", description = "Internal endpoints to manage sensitive words")
@RequestMapping("/internal/words")
public class FilteredWordController {

    private final FilteredWordService service;

    public FilteredWordController(FilteredWordService service) {
        this.service = service;
    }

    @Operation(summary = "List all filtered words")
    @GetMapping
    public List<FilteredWordResponse> list() {
        return service.listAll().stream().map(this::toResponse).toList();
    }

    @Operation(summary = "Get a filtered word by id")
    @GetMapping("/{id}")
    public FilteredWordResponse get(@PathVariable UUID id) {
        return toResponse(service.get(id));
    }

    @Operation(summary = "Create a filtered word")
    @PostMapping
    public ResponseEntity<FilteredWordResponse> create(@Valid @RequestBody FilteredWordRequest req) {
        FilteredWord created = service.create(req.getWord());
        FilteredWordResponse body = toResponse(created);
        return ResponseEntity.created(URI.create("/internal/words/" + created.getId())).body(body);
    }

    @Operation(summary = "Update a filtered word")
    @PutMapping("/{id}")
    public FilteredWordResponse update(@PathVariable UUID id, @Valid @RequestBody FilteredWordRequest req) {
        return toResponse(service.update(id, req.getWord()));
    }

    @Operation(summary = "Delete a filtered word")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Admin utility to seed from classpath list
    @Operation(summary = "Seed words from built-in list", description = "Seeds from classpath sql_sensitive_list.txt. Set onlyIfEmpty=false to force reseed.")
    @PostMapping("/seed")
    public SeedResult seed(@RequestParam(name = "onlyIfEmpty", defaultValue = "true") boolean onlyIfEmpty) {
        return service.seedFromResource(onlyIfEmpty);
    }

    private FilteredWordResponse toResponse(FilteredWord fw) {
        return new FilteredWordResponse(fw.getId(), fw.getWord());
    }
}
