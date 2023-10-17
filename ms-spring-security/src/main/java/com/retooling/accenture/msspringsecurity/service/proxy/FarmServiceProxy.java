package com.retooling.accenture.msspringsecurity.service.proxy;

import com.retooling.accenture.msspringsecurity.model.User;
import com.retooling.accenture.msspringsecurity.model.dto.FarmDTO;
import com.retooling.accenture.msspringsecurity.model.dto.FarmServiceConfigDTO;
import feign.Response;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name="api-gateway")
@RibbonClient(name="ms-farm-service")
public interface FarmServiceProxy {

    @GetMapping("/ms-farm-service/farmers")
    Object getFarmers();

    @PostMapping("/ms-farm-service/farmer")
    ResponseEntity<Object> createFarmer(@RequestBody User userCreated);

    @GetMapping("/ms-farm-service/farmersFromFarmer/{userId}")
    Object getFarmersFromUser(@PathVariable("userId") int userId);

    @GetMapping("/ms-farm-service/farm/{farmId}")
    Object getFarm(@PathVariable("farmId") int farmId);

    @PostMapping("/ms-farm-service/createConfig")
    ResponseEntity<FarmServiceConfigDTO> createConfig(@RequestBody FarmServiceConfigDTO farmServiceConfigDTO);

    @GetMapping("/ms-farm-service/getFarmServiceConfig")
    FarmServiceConfigDTO getFarmServiceConfig();

    @PostMapping("/ms-farm-service/createChickens")
    Object createChickens(@RequestParam int cantidad, @RequestParam int farmId);

    @PostMapping("/ms-farm-service/createEggs")
    Object createEggs(@RequestParam int cantidad, @RequestParam int farmId);

    @PostMapping("/ms-farm-service/farms")
    ResponseEntity<FarmDTO> createFarm(@RequestBody Object farm, @RequestParam int farmerId);

    @PostMapping("/ms-farm-service/passingTimeFarms")
    void passingTimeFarms(@RequestParam int cantidad);

    @PostMapping("/ms-farm-service/comprarProductos")
    Response comprarProductos(@RequestParam int cantidad, @RequestParam int farmerId, @RequestParam String producto);

    @PostMapping("/ms-farm-service/venderProductos")
    Response venderProductos(@RequestParam int cantidad, @RequestParam int farmId, @RequestParam String producto);

    @GetMapping("/ms-farm-service/historial/{tipoOperacion}/{userId}")
    Object getHistorialTransaccion(@PathVariable String tipoOperacion, @PathVariable("userId") int userId);



}
