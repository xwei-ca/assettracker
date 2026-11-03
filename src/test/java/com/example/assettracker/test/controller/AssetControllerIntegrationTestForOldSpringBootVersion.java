package com.example.assettracker.test.controller;

import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//import org.springframework.test.web.servlet.MockMvc;

//import com.example.assettracker.constants.AssetStatus;
//import com.example.assettracker.model.Asset;
import com.example.assettracker.repository.AssetRepository;
import com.example.assettracker.test.base.AbstractAssetControllerTestBase;

@SpringBootTest
@AutoConfigureMockMvc
public class AssetControllerIntegrationTestForOldSpringBootVersion extends AbstractAssetControllerTestBase {
    // This is an example class of integration test for Spring Boot 2.x or early 3.x using MockMvc
    // Most of the code are commented out since they won't work with the target Spring Boot version 4.1.2 of this project
    //@Autowired
    //private MockMvc mockMvc;

    @Autowired
    private AssetRepository assetRepository;

    @BeforeEach
    void clearDb() {
        assetRepository.deleteAll();
    }
/*
    @Test
    void shouldReturnAllAssets() throws Exception {
        assetRepository.save(new Asset("Dell Laptop", "LAPTOP", AssetStatus.IN_USE, "SN-001", "Alice"));

        mockMvc.perform(get("/api/assets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Dell Laptop")));
    }

    @Test
    void shouldReturnAssetById() throws Exception {
        Asset saved = assetRepository.save(
            new Asset("Dell Laptop", "LAPTOP", AssetStatus.IN_USE, "SN-001", "Alice"));

        mockMvc.perform(get("/api/assets/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("Dell Laptop")));
    }

    @Test
    void shouldReturn404WhenAssetNotFound() throws Exception {
        mockMvc.perform(get("/api/assets/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateAsset() throws Exception {
        String json = """
            {"name":"Monitor","type":"MONITOR","status":"AVAILABLE",
             "serialNumber":"SN-002","assignedTo":null}
            """;

        mockMvc.perform(post("/api/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.name", is("Monitor")));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        String json = """
            {"name":"","type":"MONITOR","status":"AVAILABLE"}
            """;

        mockMvc.perform(post("/api/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFilterByStatus() throws Exception {
        assetRepository.save(new Asset("Dell Laptop", "LAPTOP", AssetStatus.IN_USE, "SN-001", "Alice"));
        assetRepository.save(new Asset("Monitor", "MONITOR", AssetStatus.AVAILABLE, "SN-002", null));

        mockMvc.perform(get("/api/assets").param("status", "IN_USE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].status", is("IN_USE")));
    }

    @Test
    void shouldDeleteAsset() throws Exception {
        Asset saved = assetRepository.save(
            new Asset("Dell Laptop", "LAPTOP", AssetStatus.IN_USE, "SN-001", "Alice"));

        mockMvc.perform(delete("/api/assets/" + saved.getId()))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/assets/" + saved.getId()))
            .andExpect(status().isNotFound());
    }
*/

}
