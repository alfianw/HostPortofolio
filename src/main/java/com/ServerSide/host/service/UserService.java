/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service;

import com.ServerSide.host.Repository.UserRepository;
import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.UserPaginationResponse;
import com.ServerSide.host.models.Role;
import com.ServerSide.host.models.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hp
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

}
