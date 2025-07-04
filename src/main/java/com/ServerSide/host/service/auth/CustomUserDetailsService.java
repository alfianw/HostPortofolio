/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service.auth;

import com.ServerSide.host.models.User;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ServerSide.host.Repository.LoginOrRegisterRepository;

/**
 *
 * @author Hp
 */
@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private LoginOrRegisterRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        User user = userRepository.findByEmail(usernameOrEmail)
            .or(() -> userRepository.findByUserName(usernameOrEmail))
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                        .collect(Collectors.toList())
        );
    }
}
