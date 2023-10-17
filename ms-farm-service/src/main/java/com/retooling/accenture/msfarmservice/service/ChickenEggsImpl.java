package com.retooling.accenture.msfarmservice.service;


import com.retooling.accenture.msfarmservice.model.*;
import com.retooling.accenture.msfarmservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


// servicio encargado de todas las acciones para la creacion de huevos y gallinas, la configuracion de los valores y la logica
// para el paso del tiempo
@Service("ChickenEggsService")
public class ChickenEggsImpl implements ChickenEggService {
    private FarmServiceConfig farmServiceConfig;
    @Autowired
    private ChickenRepository chickenRepository;
    @Autowired
    private EggsRepository eggsRepository;
    @Autowired
    private FarmRepository farmRepository;


    //metodo para crear la configuracion
    public FarmServiceConfig createConfig(FarmServiceConfig configFarm) {
        FarmServiceConfig config = new FarmServiceConfig(configFarm);
        this.farmServiceConfig = config;
        return config;
    }

    public void setFarmServiceConfig(FarmServiceConfig farmServiceConfig) {
        this.farmServiceConfig = farmServiceConfig;
    }

    public FarmServiceConfig getFarmServiceConfig() {
        return this.farmServiceConfig;
    }

    //metodo para crear las gallinas
    public List<Chicken> createChickens(int cantidad, int farmId) {

        List<Chicken> chickensCreated = new ArrayList<>();

        Farm farm = farmRepository.findById(farmId);

        for (int i = 0; i < cantidad; i++) {
            Chicken chicken = new Chicken(farmServiceConfig.getChickensDaysToDie(), farmServiceConfig.getAmountDaysToPutEggs(), farmServiceConfig.getAmountEggsToPut());
            chicken.setFarm(farm);
            chickensCreated.add(chicken);
            chickenRepository.save(chicken);
        }

        farm.getChickens().addAll(chickensCreated);
        farm.setCantChickens(farm.getCantChickens() + cantidad);
        farm.setCapacidadDisponible();
        farmRepository.save(farm);

        return chickensCreated;

    }

    //metodo para crear los huevos
    public List<Egg> createEggs(int cantidad, int farmId) {

        List<Egg> eggsCreated = new ArrayList<>();
        Farm farm = farmRepository.findById(farmId);

        for (int i = 0; i < cantidad; i++) {
            Egg egg = new Egg(farmServiceConfig.getEggsDaysToBecomeChicken());
            egg.setFarm(farm);
            eggsCreated.add(egg);
            eggsRepository.save(egg);
        }
        farm.getEggs().addAll(eggsCreated);
        farm.setCantEggs(farm.getCantEggs() + cantidad);
        farm.setCapacidadDisponible();
        farmRepository.save(farm);
        return eggsCreated;

    }

    //metodo para convertir todos los huevos que cumplieron los días para ser gallina
    public void convertirHuevosEnGallina(int idFarm) {

        Farm farm = farmRepository.findById(idFarm);
        List<Egg> eggsFarm = farm.getEggs();

        List<Egg> eggsToConvert = eggsFarm.stream()
                .filter(egg -> egg.getDaysToBecomeChicken() == 0)
                .collect(Collectors.toList());

        if (!eggsToConvert.isEmpty()) {
            int cantidadGallinasXNacer = eggsToConvert.size();
            eggsToConvert.forEach(egg -> convertirHuevoAGallina(egg, farm));
            farm.setCantEggs(eggsFarm.size() - cantidadGallinasXNacer);
            farm.setCapacidadDisponible();
            farm.getEggs().removeAll(eggsToConvert);
            farmRepository.save(farm);
        }

    }

    public void convertirHuevoAGallina(Egg egg, Farm farm) {

        Chicken chicken = new Chicken(farmServiceConfig.getChickensDaysToDie(), farmServiceConfig.getAmountDaysToPutEggs(), farmServiceConfig.getAmountEggsToPut());
        chicken.setFarm(farm);
        chickenRepository.save(chicken);
        farm.getChickens().add(chicken);
        farm.setCantChickens(farm.getCantChickens() + 1);
        eggsRepository.deleteById(egg.getId());

    }


    //metodo para que se creen la cantidad de huevos para las gallinas que pasaron los días segun la configuracion
    public void gallinasPonenHuevos(int farmId) {

        Farm farm = farmRepository.findById(farmId);
        List<Chicken> chickens = farm.getChickens();

        List<Chicken> chickensEggs = chickens.stream().filter(c -> c.getRemainingDaysToPutEggs() == 0).collect(Collectors.toList());

        if (!chickensEggs.isEmpty()) {
            for (Chicken chicken : chickensEggs) {
                chicken.setRemainingDaysToPutEggs(farmServiceConfig.getAmountDaysToPutEggs());
                chickenRepository.save(chicken);

                for (int i = 0; i < chicken.getAmountEggsToPut(); i++) {
                    Egg egg = new Egg(farmServiceConfig.getEggsDaysToBecomeChicken());
                    egg.setFarm(farm);
                    eggsRepository.save(egg);
                    farm.getEggs().add(egg);
                    farm.setCantEggs(farm.getCantEggs() + 1);

                }
            }
            farm.setCapacidadDisponible();
            farmRepository.save(farm);
        }

    }

    //metodo para que cuando se cumple los días de vida de las gallinas, se borren.
    public void gallinasMueren(int farmId) {

        Farm farm = farmRepository.findById(farmId);
        List<Chicken> chickensFarm = farm.getChickens();

        List<Chicken> gallinasMueren = chickensFarm.stream()
                .filter(chicken -> chicken.getRemainingDaysToDie() == 0)
                .collect(Collectors.toList());

        if (!gallinasMueren.isEmpty()) {

            for (Chicken chicken : gallinasMueren) {
                chickenRepository.deleteById(chicken.getId());
            }
            farm.setCantChickens(chickensFarm.size() - gallinasMueren.size());
            farm.setCapacidadDisponible();
            farm.getChickens().removeAll(gallinasMueren);
            farmRepository.save(farm);

        }

    }


    //metodo para el paso del tiempo en una granja
    public void passingTimeChicken(int cantidadDias, int farmId) {
        Farm farm = farmRepository.findById(farmId);

        List<Chicken> chickens = farm.getChickens();
        List<Egg> eggs = farm.getEggs();

        for (int i = 0; i < cantidadDias; i++) {
            // Restar un día de vida de las gallinas
            for (Chicken chicken : chickens) {
                int diasRestantesVida = chicken.getRemainingDaysToDie();
                int diasRestantesPonerHuevos = chicken.getRemainingDaysToPutEggs();
                chicken.setRemainingDaysToDie(diasRestantesVida - 1);
                chicken.setRemainingDaysToPutEggs(diasRestantesPonerHuevos - 1);
                chickenRepository.save(chicken);
            }
            // Se hace remove gallinas que remainingDays = 0
            gallinasMueren(farmId);

            //Se busca las gallinas que tienen días restantes poner huevos en 0 y se agrega huevo a la lista
            gallinasPonenHuevos(farmId);

            // Incrementar la edad de todos los huevos en la granja
            for (Egg egg : eggs) {
                int diasRestantesConvertirseGallina = egg.getDaysToBecomeChicken();
                egg.setDaysToBecomeChicken(diasRestantesConvertirseGallina - 1);
                eggsRepository.save(egg);
            }
            //convierte los huevos que tienen diasRestantes en 0 y los agrega a la lista gallinas
            convertirHuevosEnGallina(farmId);

        }
    }

    //metodo para el paso del tiempo de todas las granjas
    public void passingTimeFarms(int cantidad){
        for (Farm farm:farmRepository.findAll()) {
            passingTimeChicken(cantidad,farm.getId());
        }
    }



}
