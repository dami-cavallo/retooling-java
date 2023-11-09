package com.retooling.accenture.msspringsecurity.service;

import com.retooling.accenture.msspringsecurity.exception.*;
import com.retooling.accenture.msspringsecurity.model.Role;
import com.retooling.accenture.msspringsecurity.model.User;
import com.retooling.accenture.msspringsecurity.model.dto.FarmDTO;
import com.retooling.accenture.msspringsecurity.model.dto.FarmServiceConfigDTO;
import com.retooling.accenture.msspringsecurity.repositories.RoleRepository;
import com.retooling.accenture.msspringsecurity.repositories.UserRepository;
import com.retooling.accenture.msspringsecurity.service.proxy.FarmServiceProxy;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private FarmServiceProxy farmServiceProxy;

    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public List<Role> getRoles(){
        return roleRepository.findAll();
    }
    public User findById(int userId) {
        return userRepository.findById(userId);
    }
    public Role createRole(Role role){
        return roleRepository.save(role);
    }
    public User getAuthUser(){
        return userRepository.findByEmail(userDetailsService.isAuth());
    }
    public User saveUser(User user, int roleId) {

        user.setUserPass(passwordEncoder.encode(user.getUserPass()));
        user.setRole(roleRepository.findById(roleId).get());
        return userRepository.save(user);

    }

    //metodo para crear el usuario y el farmer en el ms-farm-service
    public ResponseEntity<User> createUser(User user, int roleId) throws Exception{

        if (userRepository.findByEmail(user.getEmail())!=null){
            throw new UsuarioExisteException(user.getEmail());
        }
        User userCreated= saveUser(user,roleId);
        try {

            ResponseEntity<Object> responseEntity = farmServiceProxy.createFarmer(userCreated);

            if (responseEntity.getStatusCode().isError()) {
                userRepository.deleteById(userCreated.getId());
                URI location = responseEntity.getHeaders().getLocation();
                throw new UsuarioNoCreadoException("Error al crear usuario: " + responseEntity.getStatusCode() + "MS: ms-farm-service/farmer");
            }
        } catch (Exception e) {
            userRepository.deleteById(userCreated.getId());
            System.out.println("Error: " + e.getMessage());
            throw new UsuarioNoCreadoException(e.getMessage());

        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(userCreated.getId()).toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    public User updateUser(User newUser, int id){
        User oldUser = userRepository.findById(id);
        return updateDataUser(oldUser,newUser);
    }

    //metodo para actualizar los datos del user
    public User updateDataUser(User oldUser, User newUser) {
        if (newUser.getFirstName() != null && !(newUser.getFirstName().equals(oldUser.getFirstName()))) {
            oldUser.setFirstName(newUser.getFirstName());
        }
        if (newUser.getLastName() != null && !(newUser.getLastName().equals(oldUser.getLastName()))) {
            oldUser.setLastName(newUser.getLastName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().equalsIgnoreCase(oldUser.getEmail())) {
            if (userRepository.findByEmail(newUser.getEmail()) == null){
                oldUser.setEmail(newUser.getEmail());
            }
            else {
                throw new UsuarioExisteException(newUser.getEmail());
            }
        }
        if (newUser.getUserPass() != null && !(newUser.getUserPass().equals(passwordEncoder.encode(oldUser.getUserPass())))) {
            oldUser.setUserPass(passwordEncoder.encode(newUser.getUserPass()));
        }

       return oldUser;
    }

    //Armado Response al momento de actualizar los datos del usuario
    public ResponseEntity<User> updateDataUserResponse(User user,int id){

        if (userRepository.findById(id) == null) {
            throw new UsuarioNoEncontrado(String.valueOf(id));
        } else if (user != null) {
            if (userDetailsService.isAuth() != null) {
                User userUpdated = updateUser(user, id);
                userRepository.save(userUpdated);
                return new ResponseEntity<>(userUpdated, HttpStatus.OK);
            } else {
                throw new UsuarioNoAutorizadoException(userDetailsService.isAuth());
            }
        } else {
            throw new DatosErroneosException();
        }
    }

    //obtener los usuarios farmers del ms-farm-service y armar el map del objeto
    public Map<String, Object> getFarmers() {
        return maps("farmers",farmServiceProxy.getFarmers());
    }

    //obtener las granjas del usuario del get de farms en el ms-farm-service y armar el map con la lista de granjas
    public Map<String, Object> getFarmersFromFarmer(int userId) {
        return maps("farms", farmServiceProxy.getFarmersFromUser(userId));
    }

    //obtener una granja específica y armar un map con el objeto.
    public Map<String, Object> getFarm(int farmId) {
        return maps("farm", farmServiceProxy.getFarm(farmId));
    }

    //utilizar el post del ms-farm-service para los valores de configuracion
    public ResponseEntity<FarmServiceConfigDTO> createConfig(FarmServiceConfigDTO farmServiceConfigDTO){
        return farmServiceProxy.createConfig(farmServiceConfigDTO);
    }

    //servicio creacion de granja del ms-farm-service
    public ResponseEntity<FarmDTO> createFarm(FarmDTO farm, int farmerId){
        return farmServiceProxy.createFarm(farm,farmerId);
    }

    //obtener los valores de configuracion del ms-farm-service
    public FarmServiceConfigDTO getFarmServiceConfig(){
        return farmServiceProxy.getFarmServiceConfig();
    }


    //servicio de creacion de gallinas en el ms-farm-service
    public ResponseEntity<?> createChickens(int cantidad, int farmId){

        Map<String, String> responseJson = new HashMap<>();
        String message;
        try {

            if (userDetailsService.isAuth()!= null && userDetailsService.getRole(userDetailsService.isAuth()).matches("ADMIN")){

                Response response = farmServiceProxy.createChickens(cantidad, farmId);
                message = response.body().toString();

                if (response.status() == HttpStatus.OK.value()) {

                    responseJson.put("message", message);
                    return new ResponseEntity<>(responseJson, HttpStatus.OK);

                } else {

                    responseJson.put("message", message);
                    return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
            else

                throw new UsuarioNoAutorizadoException(userDetailsService.isAuth());


        } catch (Exception e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //servicio de creacion de huevos en el ms-farm-service
    public ResponseEntity<?> createEggs(int cantidad, int farmId){

        Map<String, String> responseJson = new HashMap<>();
        String message;

        try {

            if (userDetailsService.isAuth() != null && userDetailsService.getRole(userDetailsService.isAuth()).matches("ADMIN")) {

                Response response = farmServiceProxy.createEggs(cantidad, farmId);

                message = response.body().toString();

                if (response.status() == HttpStatus.OK.value()) {

                    responseJson.put("message", message);
                    return new ResponseEntity<>(responseJson, HttpStatus.OK);

                } else {

                    responseJson.put("message", message);
                    return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else
                throw new UsuarioNoAutorizadoException(userDetailsService.isAuth());

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }


    //servicio del paso del tiempo del ms-farm-service
    public void passingTimeFarms(int cantidad){

        if (userDetailsService.isAuth()!= null && userDetailsService.getRole(userDetailsService.isAuth()).matches("ADMIN")){
            User userAuth = userRepository.findByEmail(userDetailsService.isAuth());
            farmServiceProxy.passingTimeFarms(cantidad);
        }
        else
            throw new UsuarioNoAutorizadoException(userDetailsService.isAuth());
    }

    //servicio de compra del ms-farm-service
    public ResponseEntity<?> comprarProductos(int cantidad, int farmerId, String producto){
        Map<String, String> responseJson = new HashMap<>();
        String message;

        try {

            Response response = farmServiceProxy.comprarProductos(cantidad,farmerId,producto);

            if (response.status() == HttpStatus.OK.value()){
                message = response.body().toString();
                responseJson.put("message",message);
                return new ResponseEntity<>(responseJson,HttpStatus.OK);
            }
            else {
                message = response.body().toString();
                responseJson.put("message", message);
                return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    //servicio de venta del ms-farm-service

    public ResponseEntity<?> venderProductos(int cantidad, int farmId, String producto){

        Map<String, String> responseJson = new HashMap<>();
        String message;

        try (Response response = farmServiceProxy.venderProductos(cantidad, farmId, producto)) {
            if (response.status() == HttpStatus.OK.value()) {
                message = response.body().toString();
                responseJson.put("message", message);
                return new ResponseEntity<>(responseJson, HttpStatus.OK);
            } else {
                message = response.body().toString();
                responseJson.put("message", message);
                return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //servicio para obtener el historial del ms-farm-service
    public Map<String, Object> getHistorial(String tipoOperacion,int userId) {
        return maps("transacciones", farmServiceProxy.getHistorialTransaccion(tipoOperacion,userId));
    }

    //metodo para crear un map con la clave string y un objeto
    public Map<String, Object> maps(String var, Object some) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(var, some);
        return dto;
    }



}