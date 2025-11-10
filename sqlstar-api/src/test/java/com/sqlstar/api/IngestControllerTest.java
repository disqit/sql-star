package com.sqlstar.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqlstar.dto.IngestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import com.sqlstar.test.BaseDbTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IngestControllerTest extends BaseDbTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postSqlstarAcceptsJson() throws Exception {
        IngestRequest req = new IngestRequest();
        req.setQuery("select 1");
        req.setMetadata(Map.of("k", "v"));
        String json = objectMapper.writeValueAsString(req);

        mockMvc.perform(post("/sqlstar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.amendedText").exists())
                .andExpect(jsonPath("$.replacements").isNumber());
    }

    @Test
    void trailingSlashAlsoWorks() throws Exception {
        String json = "{\"query\":\"select 2\"}";
        mockMvc.perform(post("/sqlstar/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.amendedText").exists())
                .andExpect(jsonPath("$.replacements").isNumber());
    }

    @Test
    void validationFailsOnBlankQuery() throws Exception {
        String json = "{\"query\":\"   \"}";
        mockMvc.perform(post("/sqlstar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
