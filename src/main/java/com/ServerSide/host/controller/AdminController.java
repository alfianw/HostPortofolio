/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.controller;

import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.ApiResponsePagination;
import com.ServerSide.host.dto.DetailProfileResponse;
import com.ServerSide.host.dto.EditUserRoleRequest;
import com.ServerSide.host.dto.UserPaginationRequest;
import com.ServerSide.host.dto.UserPaginationResponse;
import com.ServerSide.host.service.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //pagination
    @PostMapping("/userPagination")
    public ResponseEntity<ApiResponsePagination<List<UserPaginationResponse>>> getUsers(@RequestBody UserPaginationRequest request) {
        return ResponseEntity.ok(adminService.getUsersWithPagination(request));
    }

    //detele
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id){
        adminService.deleteUserById(id);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                .responseCode("00")
                .responseMessage("Detele User Success")
                .data(null)
                .build()
        );
    }
    
    @PutMapping("/update-status-role")
    public ResponseEntity<ApiResponse<DetailProfileResponse>> updateStatusAndRole(@RequestBody 
            EditUserRoleRequest request
    ){
     return ResponseEntity.ok(adminService.updateStatusAndRole(request));
    }
}
