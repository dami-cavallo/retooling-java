package com.retooling.accenture.msspringsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.persistence.EntityExistsException;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UsuarioExisteException extends EntityExistsException {
    public UsuarioExisteException(String username){
        super(String.format("El usuario %s ya existe", username));
    }
}
