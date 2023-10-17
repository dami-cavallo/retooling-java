package com.retooling.accenture.msspringsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UsuarioNoEncontrado extends RuntimeException {
    public UsuarioNoEncontrado(String valor) {
        super(String.format("No se encontro el usuario %s",valor));
    }
}
