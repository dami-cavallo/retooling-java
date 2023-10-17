package com.retooling.accenture.msfarmservice.controller;

import com.retooling.accenture.msfarmservice.model.*;
import com.retooling.accenture.msfarmservice.repositories.FarmerRepository;
import com.retooling.accenture.msfarmservice.repositories.TransaccionRepository;
import com.retooling.accenture.msfarmservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
public class FarmerController {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private FarmerService farmerService;

    @Autowired
    private ChickenEggService chickenEggsService;

    @Autowired
    private CompraVenta compraVentaService;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @GetMapping(path = "/farmers")
    public List<Farmer> getFarmers(){
        return farmerRepository.findAll();

    }

    @GetMapping(path = "/farmer/{id}")
    public Farmer getFarmer(@PathVariable int id){
        return farmerService.findByUserId(id);

    }

    @PostMapping("/farmer")
    public ResponseEntity<Farmer> createFarmer(@RequestBody UserBean user){

        Farmer farmerCreated = farmerService.saveFarmer(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(farmerCreated.getUserId()).toUri();

        return ResponseEntity.created(location).body(farmerCreated);

    }

    @GetMapping(path = "/farmersFromFarmer/{userId}")
    public List<Farm> getFarmersFromUser(@PathVariable int userId){

        return farmerRepository.findByUserId(userId).getFarms();

    }

    @PostMapping("/createConfig")
    public ResponseEntity<FarmServiceConfig> createConfig(@RequestBody FarmServiceConfig farmConfig){
        chickenEggsService.createConfig(farmConfig);
        return new ResponseEntity<>(chickenEggsService.getFarmServiceConfig(), HttpStatus.CREATED);
    }

    @GetMapping("/getFarmServiceConfig")
    public FarmServiceConfig getFarmServiceConfig(){
        return chickenEggsService.getFarmServiceConfig();
    }

    @PostMapping("/createChickens")
    public List<Chicken> createChickens(@RequestParam int cantidad,@RequestParam int farmId){
        return chickenEggsService.createChickens(cantidad,farmId);

    }

    @PostMapping("/createEggs")
    public List<Egg> createEggs(@RequestParam int cantidad,@RequestParam Integer farmId){
        return chickenEggsService.createEggs(cantidad,farmId);
    }


    @PostMapping(path = "/comprarProductos")
    public ResponseEntity<?> comprar(@RequestParam int cantidad,@RequestParam int farmerId,@RequestParam String producto){
        return compraVentaService.comprar(cantidad,farmerId,producto);
    }

    @PostMapping(path = "/venderProductos")
    public ResponseEntity<?> vender(@RequestParam int cantidad, @RequestParam int farmId, @RequestParam String producto){
        return compraVentaService.vender(cantidad,farmId,producto);

    }

    @GetMapping(path = "/historial/{tipoOperacion}/{userId}")
    public List<Transaccion> getHistorial(@PathVariable String tipoOperacion, @PathVariable int userId){
        TipoTransaccionProducto transaccionProducto = TipoTransaccionProducto.valueOf(tipoOperacion.toUpperCase());
        return transaccionRepository.findByTipoTransaccionAndFarmerUserIdOrderByFechaDesc(transaccionProducto,userId);

    }


}
