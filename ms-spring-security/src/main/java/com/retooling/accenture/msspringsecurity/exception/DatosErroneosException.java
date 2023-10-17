package com.retooling.accenture.msspringsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DatosErroneosException extends RuntimeException {
    public DatosErroneosException(){
        super("Datos incorrectos");
    }
}
