package com.retooling.accenture.msfarmservice.service;


import com.retooling.accenture.msfarmservice.model.FarmServiceConfig;
import org.springframework.http.ResponseEntity;


public interface ChickenEggService {

    void convertirHuevosEnGallina(int idFarm);

    void gallinasPonenHuevos(int farmId);

    void gallinasMueren(int farmId);

    void passingTimeChicken(int cantidadDias, int farmId);

    void passingTimeFarms(int cantidad);

    ResponseEntity createEggs(int cantidad, int farmId);

    FarmServiceConfig getFarmServiceConfig();

    ResponseEntity createChickens(int cantidad, int farmId);

    FarmServiceConfig createConfig(FarmServiceConfig configFarm);




}
