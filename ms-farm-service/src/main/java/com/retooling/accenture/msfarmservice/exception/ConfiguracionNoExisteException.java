package com.retooling.accenture.msfarmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ConfiguracionNoExisteException extends RuntimeException {
    public ConfiguracionNoExisteException(String message) {
        super(message);
    }
}
