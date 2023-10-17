package com.retooling.accenture.msspringsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UsuarioNoAutorizadoException extends RuntimeException {
    public UsuarioNoAutorizadoException(String username) {
        super(String.format("El usuario %s no esta autorizado", username));
    }
}
