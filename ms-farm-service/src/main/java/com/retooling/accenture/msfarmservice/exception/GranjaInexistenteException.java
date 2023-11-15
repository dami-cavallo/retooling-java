package com.retooling.accenture.msfarmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class GranjaInexistenteException extends RuntimeException{
    public GranjaInexistenteException(String message) {
        super(message);
    }
}
