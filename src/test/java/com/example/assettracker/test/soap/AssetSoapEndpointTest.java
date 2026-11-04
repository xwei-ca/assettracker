package com.example.assettracker.test.soap;

import com.example.assettracker.constants.AssetStatus;
import com.example.assettracker.model.Asset;
import com.example.assettracker.service.AssetService;
import com.example.assettracker.soap.AssetSoapEndpoint;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webservices.test.autoconfigure.server.WebServiceServerTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.*;

@WebServiceServerTest(AssetSoapEndpoint.class)

class AssetSoapEndpointTest {
    @Autowired
    private MockWebServiceClient mockClient;

    @MockitoBean
    private AssetService assetService;

    @Test
    void getAssetShouldReturnAsset() {
        Asset asset = new Asset("Dell Laptop", "LAPTOP", AssetStatus.IN_USE, "SN-001", "Alice");
        asset.setId(1L);
        when(assetService.findById(1L)).thenReturn(asset);

        StringSource request = new StringSource("""
            <getAssetRequest xmlns="http://example.com/assettracker/soap">
                <id>1</id>
            </getAssetRequest>
            """);

        StringSource expectedResponse = new StringSource("""
            <getAssetResponse xmlns="http://example.com/assettracker/soap">
                <asset>
                    <id>1</id>
                    <name>Dell Laptop</name>
                    <type>LAPTOP</type>
                    <status>IN_USE</status>
                    <serialNumber>SN-001</serialNumber>
                    <assignedTo>Alice</assignedTo>
                </asset>
            </getAssetResponse>
            """);

        mockClient.sendRequest(withPayload(request))
            .andExpect(payload(expectedResponse));
    }

    @Test
    void getAssetShouldReturnFaultWhenNotFound() {
        when(assetService.findById(999L))
            .thenThrow(new com.example.assettracker.exception.RecordNotFoundException("not found"));

        StringSource request = new StringSource("""
            <getAssetRequest xmlns="http://example.com/assettracker/soap">
                <id>999</id>
            </getAssetRequest>
            """);

        mockClient.sendRequest(withPayload(request))
            .andExpect(clientOrSenderFault("Asset not found"));
    }

}
