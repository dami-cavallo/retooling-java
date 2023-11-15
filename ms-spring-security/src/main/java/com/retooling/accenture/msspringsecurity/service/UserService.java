package com.retooling.accenture.msspringsecurity.service;

import com.retooling.accenture.msspringsecurity.model.Role;
import com.retooling.accenture.msspringsecurity.model.User;
import com.retooling.accenture.msspringsecurity.model.dto.FarmDTO;
import com.retooling.accenture.msspringsecurity.model.dto.FarmServiceConfigDTO;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Map;

public interface UserService {
    User findById(int userId);
    User getAuthUser();
    User saveUser(User user,int roleId);
    User updateUser(User user, int id);
    List<User> getUsers();
    List<Role> getRoles();
    Role createRole(Role role);
    ResponseEntity<User> createUser(User user, int roleId) throws Exception;
    ResponseEntity<User> updateDataUserResponse(User user,int id);
    Map<String, Object> getFarmers();
    Map<String, Object> getFarmersFromFarmer(int userId);
    Map<String, Object> getFarm(int farmId);
    ResponseEntity<FarmServiceConfigDTO> createConfig(FarmServiceConfigDTO farmServiceConfigDTO);
    FarmServiceConfigDTO getFarmServiceConfig();
    ResponseEntity<?> createChickens(int cantidad, int farmId);
    ResponseEntity<?> createEggs(int cantidad, int farmId);
    void passingTimeFarms(int cantidad);
    ResponseEntity<FarmDTO> createFarm(FarmDTO farm, int farmerId);
    ResponseEntity<?> comprarProductos(int cantidad, int farmerId, String producto, int granjaOrigen);
    ResponseEntity<?> venderProductos(int cantidad, int farmId, String producto, int granjaDestino);
    Map<String, Object> getHistorial(String tipoOperacion,int userId);
}
