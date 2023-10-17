package com.retooling.accenture.msspringsecurity.service;

import com.retooling.accenture.msspringsecurity.model.User;
import com.retooling.accenture.msspringsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //servicio para login y autenticacion
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getUserPass())
                .roles(user.getRole().getRoleName()) // Asigna los roles necesarios según tus requisitos
                .build();
    }

    public String isAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        } else {
            return authentication.getName();
        }
    }

    public String getRole(String email) {
        User user = userRepository.findByEmail(email);
        return user.getRole().getRoleName();
    }

}
