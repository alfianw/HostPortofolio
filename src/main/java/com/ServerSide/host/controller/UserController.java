/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.controller;

import com.ServerSide.host.dto.ApiResponsePagination;
import com.ServerSide.host.dto.UserPaginationRequest;
import com.ServerSide.host.dto.UserPaginationResponse;
import com.ServerSide.host.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApiResponsePagination<List<UserPaginationResponse>>> getUsers(@RequestBody UserPaginationRequest request) {
        return ResponseEntity.ok(userService.getUsersWithPagination(request));
    }
}
