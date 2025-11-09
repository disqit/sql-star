package com.sqlstar.api;

import com.sqlstar.dto.FilteredWordRequest;
import com.sqlstar.dto.FilteredWordResponse;
import com.sqlstar.dto.SeedResult;
import com.sqlstar.model.FilteredWord;
import com.sqlstar.service.FilteredWordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/words")
public class FilteredWordController {

    private final FilteredWordService service;

    public FilteredWordController(FilteredWordService service) {
        this.service = service;
    }

    @GetMapping
    public List<FilteredWordResponse> list() {
        return service.listAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public FilteredWordResponse get(@PathVariable UUID id) {
        return toResponse(service.get(id));
    }

    @PostMapping
    public ResponseEntity<FilteredWordResponse> create(@Valid @RequestBody FilteredWordRequest req) {
        FilteredWord created = service.create(req.getWord());
        FilteredWordResponse body = toResponse(created);
        return ResponseEntity.created(URI.create("/internal/words/" + created.getId())).body(body);
    }

    @PutMapping("/{id}")
    public FilteredWordResponse update(@PathVariable UUID id, @Valid @RequestBody FilteredWordRequest req) {
        return toResponse(service.update(id, req.getWord()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Admin utility to seed from classpath list
    @PostMapping("/seed")
    public SeedResult seed(@RequestParam(name = "onlyIfEmpty", defaultValue = "true") boolean onlyIfEmpty) {
        return service.seedFromResource(onlyIfEmpty);
    }

    private FilteredWordResponse toResponse(FilteredWord fw) {
        return new FilteredWordResponse(fw.getId(), fw.getWord());
    }
}
