package com.retooling.accenture.msfarmservice.controller;

import com.retooling.accenture.msfarmservice.model.Farm;
import com.retooling.accenture.msfarmservice.repositories.FarmRepository;
import com.retooling.accenture.msfarmservice.service.ChickenEggService;
import com.retooling.accenture.msfarmservice.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class FarmController {
    @Autowired
    private FarmService farmService;

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private ChickenEggService chickenEggsService;

    @GetMapping(path = "/farm/{id}")
    public Farm getFarm(@PathVariable int id){
        return farmService.findById(id);
    }

    @GetMapping(path = "/farms/{farmerId}")
    public List<Farm> getFarmsFromFarmer(@PathVariable int farmerId){
        return farmService.findByFarmerUserId(farmerId);

    }

    @PostMapping(path = "/farms")
    public ResponseEntity<Farm> createFarm(@RequestBody Farm farm,@RequestParam int farmerId){
        Farm farmsCreated = farmService.createFarm(farm,farmerId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{farmerId}")
                .buildAndExpand(farmerId).toUri();
        return ResponseEntity.created(location).body(farmsCreated);
    }
    @PostMapping(path = "/convertirHuevosEnGallina")
    public void convertirHuevosEnGallina(@RequestParam int farmId){
        chickenEggsService.convertirHuevosEnGallina(farmId);

    }

    @PostMapping(path = "/gallinasPonenHuevos")
    public void gallinasPonenHuevos(@RequestParam int farmId){
        chickenEggsService.gallinasPonenHuevos(farmId);

    }

    @PostMapping(path = "/gallinasMueren")
    public void gallinasMueren(@RequestParam int farmId){
        chickenEggsService.gallinasMueren(farmId);
    }

    @PostMapping(path = "/passingTimeChicken")
    public void passingTimeChicken(@RequestParam int cantidadDias,@RequestParam int farmId){
        chickenEggsService.passingTimeChicken(cantidadDias,farmId);
    }

    @PostMapping(path = "/passingTimeFarms")
    public void passingTimeFarms(@RequestParam int cantidad){
        chickenEggsService.passingTimeFarms(cantidad);
    }


}
