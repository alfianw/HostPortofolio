/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service;

import com.ServerSide.host.Repository.RoleRepository;
import com.ServerSide.host.dto.LoginForm;
import com.ServerSide.host.dto.LoginResponse;
import com.ServerSide.host.dto.RegisterForm;
import com.ServerSide.host.exception.FormatException;
import com.ServerSide.host.exception.MandatoryFieldException;
import com.ServerSide.host.exception.ResourceNotFoundException;
import com.ServerSide.host.models.Role;
import com.ServerSide.host.models.User;
import com.ServerSide.host.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ServerSide.host.Repository.LoginOrRegisterRepository;

/**
 *
 * @author Hp
 */
@Service
@RequiredArgsConstructor
public class LoginOrRegisterService {

    private final LoginOrRegisterRepository loginOrRegisRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public LoginResponse register(RegisterForm request) {

        //check duplicated email
        loginOrRegisRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new FormatException("Email Already Exists");
                });
        //check duplicate user name
        loginOrRegisRepository.findByUserName(request.getUser_Name())
                .ifPresent(u -> {
                    throw new FormatException("User Name Already Exists");
                });

        //Validasi Field
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new MandatoryFieldException("Email cannot be empty");
        } else if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new MandatoryFieldException("Password cannot be empty");
        } else if (request.getUser_Name() == null || request.getUser_Name().isBlank()) {
            throw new MandatoryFieldException("User Name cannot be empty");
        } else if (!request.getEmail().contains("@")) {
            throw new FormatException("Incorrect email format");
        }

        //add data
        User user = new User();
        user.setUserName(request.getUser_Name());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive("Y");
        //getRole
        Role roleMember = roleRepository.findByRole("MEMBER")
                .orElseThrow(() -> new RuntimeException("Role MEMBER Not Found"));

        user.getRoles().add(roleMember);
        user.setName(request.getName());
        loginOrRegisRepository.save(user);

        // get token after register success
        String token = jwtTokenProvider.generateToken(user);

        return new LoginResponse(
                "00",
                "Success",
                token,
                user.getUserName(),
                user.getEmail()
        );
    }

    public LoginResponse login(LoginForm loginRequest) {
        User user = loginOrRegisRepository.findByEmail(loginRequest.getIdentifier())
                .or(() -> loginOrRegisRepository.findByUserName(loginRequest.getIdentifier()))
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new FormatException("Password is incorrect");
        }

        String token = jwtTokenProvider.generateToken(user);

        return new LoginResponse(
                "00",
                "Success",
                token,
                user.getUserName(),
                user.getEmail()
        );
    }
}
