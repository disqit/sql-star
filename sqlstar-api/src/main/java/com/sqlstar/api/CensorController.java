package com.sqlstar.api;

import com.sqlstar.dto.CensorRequest;
import com.sqlstar.dto.CensorResponse;
import com.sqlstar.service.MessageFilterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/censor")
public class CensorController {

    private final MessageFilterService messageFilterService;

    public CensorController(MessageFilterService messageFilterService) {
        this.messageFilterService = messageFilterService;
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<CensorResponse> censor(@Valid @RequestBody CensorRequest request) {
        MessageFilterService.Result result = messageFilterService.censor(request.getText());
        return ResponseEntity.ok(new CensorResponse(result.amendedText(), result.replacements()));
    }
}
