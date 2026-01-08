package com.prem_table_soap.backend;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT )
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {

        super(message);
    }
}
