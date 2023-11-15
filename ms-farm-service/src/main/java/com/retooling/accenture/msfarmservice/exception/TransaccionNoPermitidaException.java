package com.retooling.accenture.msfarmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class TransaccionNoPermitidaException extends RuntimeException {
    public TransaccionNoPermitidaException(String message) {
        super(message);
    }
}
