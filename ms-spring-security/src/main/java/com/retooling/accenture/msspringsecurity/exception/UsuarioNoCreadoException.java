package com.retooling.accenture.msspringsecurity.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UsuarioNoCreadoException extends Exception {
    public UsuarioNoCreadoException(String error) {
        super(error);
    }
}
