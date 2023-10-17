package com.retooling.accenture.msspringsecurity.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;


public class MyCustomErrorDecoder implements ErrorDecoder {

    //Clase para formatear los errores que devuelve el ms-farm-service
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.BAD_REQUEST.value()) {
            // formatear el error
            String errorMessage = "Error: " + response.body();
            return new RuntimeException(errorMessage);
        }
        // En caso de error desconocido, devolver una excepción genérica
        return new RuntimeException("Error desconocido");
    }
}
