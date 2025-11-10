package com.sqlstar.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;
import com.sqlstar.test.BaseDbTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilteredWordControllerTest extends BaseDbTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String randomWord() {
        return "WORD_" + UUID.randomUUID();
    }

    @Test
    void createListGetUpdateDeleteFlow() throws Exception {
        String initial = randomWord();
        String json = objectMapper.writeValueAsString(Map.of("word", initial));

        // Create
        String location = mockMvc.perform(post("/internal/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.word").value(initial))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        // List includes created
        mockMvc.perform(get("/internal/words"))
                .andExpect(status().isOk());

        // Get by id
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value(initial));

        // Update word
        String updated = initial + "_UPDATED";
        String updateJson = objectMapper.writeValueAsString(Map.of("word", updated));
        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value(updated));

        // Delete
        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());

        // Get after delete -> 404
        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    @Test
    void duplicateWordRejectedCaseInsensitive() throws Exception {
        String word = randomWord().toLowerCase();
        String body1 = objectMapper.writeValueAsString(Map.of("word", word));
        String body2 = objectMapper.writeValueAsString(Map.of("word", word.toUpperCase()));

        mockMvc.perform(post("/internal/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/internal/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body2))
                .andExpect(status().isConflict());
    }
}
