package com.retooling.accenture.msspringsecurity.controller;

import com.retooling.accenture.msspringsecurity.exception.UsuarioNoEncontrado;
import com.retooling.accenture.msspringsecurity.model.LoginCredentials;
import com.retooling.accenture.msspringsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials loginCredentials) {

        try {

            if (userRepository.findByEmail(loginCredentials.getEmail())==null){
                throw new UsuarioNoEncontrado(loginCredentials.getEmail());
            }
            // Crear objeto de autenticación con el nombre de usuario y contraseña
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(), loginCredentials.getUserPass());
            // Realizar la autenticación
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Devolver si la autenticación es exitosa
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            // En caso de fallo en la autenticación, devolver una respuesta de error
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error en el inicio de sesión: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
