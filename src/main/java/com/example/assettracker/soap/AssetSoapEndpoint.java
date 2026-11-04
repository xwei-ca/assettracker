package com.example.assettracker.soap;

import com.example.assettracker.exception.AssetSoapFaultException;
import com.example.assettracker.exception.RecordNotFoundException;
import com.example.assettracker.service.AssetService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AssetSoapEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/assettracker/soap";

    private final AssetService assetService;

    public AssetSoapEndpoint(AssetService assetService) {
        this.assetService = assetService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAssetRequest")
    @ResponsePayload
    public GetAssetResponse getAsset(@RequestPayload GetAssetRequest request) {
        GetAssetResponse response = new GetAssetResponse();

        com.example.assettracker.model.Asset entity;
        try {
            entity = assetService.findById(request.getId());
        } catch (RecordNotFoundException ex) {
            throw new AssetSoapFaultException(
                "Asset not found with ID: " + request.getId());
        }

        com.example.assettracker.soap.Asset soapAsset =
                new com.example.assettracker.soap.Asset();
        soapAsset.setId(entity.getId());
        soapAsset.setName(entity.getName());
        soapAsset.setType(entity.getType());
        // entity.getStatus() returns enum AssetStatus
        soapAsset.setStatus(entity.getStatus().toString());
        soapAsset.setSerialNumber(entity.getSerialNumber());
        soapAsset.setAssignedTo(entity.getAssignedTo());

        response.setAsset(soapAsset);

        return response;
    }
}