package com.example.assettracker.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT,
           faultStringOrReason = "Asset not found")
public class AssetSoapFaultException extends RuntimeException {

    public AssetSoapFaultException(String message) {
        super(message);
    }

}
