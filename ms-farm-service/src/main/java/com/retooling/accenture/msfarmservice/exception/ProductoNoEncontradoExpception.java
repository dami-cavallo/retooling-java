package com.retooling.accenture.msfarmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProductoNoEncontradoExpception extends RuntimeException {
        public ProductoNoEncontradoExpception(String message) {
            super(message);
        }

}

