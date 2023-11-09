package com.retooling.accenture.msfarmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SinCapacidadDisponibleException extends RuntimeException{
    public SinCapacidadDisponibleException(String message) {
        super(message);
    }
}
