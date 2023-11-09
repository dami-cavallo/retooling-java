package com.retooling.accenture.msspringsecurity.controller;

import com.retooling.accenture.msspringsecurity.model.Role;
import com.retooling.accenture.msspringsecurity.model.User;
import com.retooling.accenture.msspringsecurity.model.dto.FarmDTO;
import com.retooling.accenture.msspringsecurity.model.dto.FarmServiceConfigDTO;
import com.retooling.accenture.msspringsecurity.repositories.RoleRepository;
import com.retooling.accenture.msspringsecurity.repositories.UserRepository;
import com.retooling.accenture.msspringsecurity.service.UserDetailsServiceImpl;
import com.retooling.accenture.msspringsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping(path = "/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/getAuthUser")
    public User getAuthUser(){
         return userService.getAuthUser();
    }

    @GetMapping(path = "/roles")
    public List<Role> getRoles() {
        return userService.getRoles();
    }

    @GetMapping(path = "/user/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findById(id);
    }
    @PostMapping(path = "/role")
    public void createRole(@RequestBody Role role) {
        userService.createRole(role);
    }

    @PostMapping(path = "/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, @RequestParam int roleId) throws Exception {
        return userService.createUser(user, roleId);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateDataUser(@RequestBody User user, @PathVariable("id") int id) {
        return userService.updateDataUserResponse(user, id);
    }

    @GetMapping(path = "/farmers")
    public Map<String,Object> getFarmers() {
        return userService.getFarmers();
    }

    @GetMapping(path = "/farmsFromFarmer/{userId}")
    public Map<String,Object> getFarmsFromFarmer(@PathVariable("userId") int userId) {
        return userService.getFarmersFromFarmer(userId);
    }

    @GetMapping(path = "/farm/{farmId}")
    public Map<String, Object> getFarm(@PathVariable("farmId") int farmId){
        return userService.getFarm(farmId);
    }

    @PostMapping(path = "/createConfig")
    public ResponseEntity<FarmServiceConfigDTO> createConfig(@RequestBody FarmServiceConfigDTO farmServiceConfig){
        return userService.createConfig(farmServiceConfig);
    }

    @GetMapping(path = "/getFarmServiceConfig")
    public FarmServiceConfigDTO getFarmServiceConfig(){
        return userService.getFarmServiceConfig();
    }

    @PostMapping(path = "/createChickens")
    public ResponseEntity<?> createChickens(@RequestParam int cantidad,@RequestParam int farmId){
        return userService.createChickens(cantidad,farmId);
    }

    @PostMapping(path = "/createEggs")
    public ResponseEntity<?> createEggs(@RequestParam int cantidad,@RequestParam int farmId){
        return userService.createEggs(cantidad,farmId);
    }

    @PostMapping(path = "/passingTimeFarms")
    public ResponseEntity<String> passingTimeFarms(@RequestParam int cantidad){
        try {
            userService.passingTimeFarms(cantidad);
            return new ResponseEntity<>("Pasaron " + cantidad + " dias", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al ejecutar el paso del tiempo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping(path = "/createFarm")
    public ResponseEntity<FarmDTO> createFarm(@RequestBody FarmDTO farm,@RequestParam int farmerId){
        return userService.createFarm(farm,farmerId);
    }

    @PostMapping(path = "/comprarProductos")
    public ResponseEntity<?> comprarProductos(@RequestParam int cantidad,@RequestParam int farmerId, @RequestParam String producto){
        return userService.comprarProductos(cantidad,farmerId,producto);
    }

    @PostMapping(path = "/venderProductos")
    public ResponseEntity<?> venderProductos(@RequestParam int cantidad, @RequestParam int farmId, @RequestParam String producto){

        return userService.venderProductos(cantidad,farmId,producto);

    }

    @GetMapping(path = "/historial/{tipoOperacion}/{userId}")
    public Map<String,Object> getFarmersFromFarmer(@PathVariable("tipoOperacion") String tipoOperacion, @PathVariable("userId") int userId) {
        return userService.getHistorial(tipoOperacion,userId);
    }

}
