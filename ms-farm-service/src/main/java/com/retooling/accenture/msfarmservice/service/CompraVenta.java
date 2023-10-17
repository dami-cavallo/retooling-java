package com.retooling.accenture.msfarmservice.service;

import org.springframework.http.ResponseEntity;

public interface CompraVenta {

    ResponseEntity comprar(int cantidad, int farmerId, String producto);

    ResponseEntity vender(int cantidad, int farmId, String producto);


}
