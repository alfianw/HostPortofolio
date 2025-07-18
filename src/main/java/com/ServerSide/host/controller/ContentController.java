/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.controller;

import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.InsertContentRequest;
import com.ServerSide.host.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ContentController {
    
    private final ContentService contentService;
    
    @PostMapping(value = "/insert-content", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> insertContent(
            @ModelAttribute InsertContentRequest request,
            Authentication authentication ){
    
        String email = authentication.getName();
        ApiResponse response = contentService.insertContent(request, email);
        return ResponseEntity.ok(response);
    }
}
