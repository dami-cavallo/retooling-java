package com.retooling.accenture.msfarmservice.service;

import com.retooling.accenture.msfarmservice.model.Chicken;
import com.retooling.accenture.msfarmservice.model.Egg;
import com.retooling.accenture.msfarmservice.model.FarmServiceConfig;

import java.util.List;

public interface ChickenEggService {

    void convertirHuevosEnGallina(int idFarm);

    void gallinasPonenHuevos(int farmId);

    void gallinasMueren(int farmId);

    void passingTimeChicken(int cantidadDias, int farmId);

    void passingTimeFarms(int cantidad);

    List<Egg> createEggs(int cantidad, int farmId);

    FarmServiceConfig getFarmServiceConfig();

    List<Chicken> createChickens(int cantidad, int farmId);

    FarmServiceConfig createConfig(FarmServiceConfig configFarm);




}
