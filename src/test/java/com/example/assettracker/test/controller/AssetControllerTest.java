package com.example.assettracker.test.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.assettracker.controller.AssetController;
import com.example.assettracker.exception.RecordNotFoundException;
import com.example.assettracker.model.Asset;
import com.example.assettracker.service.AssetService;
import com.example.assettracker.test.base.AbstractAssetControllerTestBase;

@WebMvcTest(AssetController.class)
@AutoConfigureRestTestClient
class AssetControllerTest extends AbstractAssetControllerTestBase {
    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private AssetService assetService;

    @BeforeEach
    void resetMocks() {
        org.mockito.Mockito.reset(assetService);
    }

    @Test
    void getByIdShouldReturnAsset() throws Exception{
        // asset was set in the super class
        when(assetService.findById(1L)).thenReturn(asset);
        
        restTestClient.get().uri("/api/assets/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Asset.class)
            .value(item -> {
                // AssertJ assertions on the typed object
                assertThat(item.getName()).isEqualTo(asset.getName());
                assertThat(item.getType()).isEqualTo(asset.getType());
                assertThat(item.getStatus()).isEqualTo(asset.getStatus());
                assertThat(item.getSerialNumber()).isEqualTo(asset.getSerialNumber());
                assertThat(item.getAssignedTo()).isEqualTo(asset.getAssignedTo());
                assertThat(item.getId()).isEqualTo(asset.getId());
            });
    }

    @Test
    void getByIdShouldReturn404WhenNotFound() {
        when(assetService.findById(999L))
            .thenThrow(new RecordNotFoundException("Asset not found with ID: 999"));

        restTestClient.get().uri("/api/assets/999")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void createShouldReturn400WhenNameBlank() {
        restTestClient.post().uri("/api/assets")
            .contentType(MediaType.APPLICATION_JSON)
            .body("""
                {"name":"","type":"LAPTOP","status":"IN_USE"}
                """)
            .exchange()
            .expectStatus().isBadRequest();
    }    
}
