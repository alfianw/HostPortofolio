/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.exception;

import com.ServerSide.host.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Hp
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(new ApiResponse<>("40", e.getMessage(), null));
    }
    
    @ExceptionHandler(MandatoryFieldException.class)
    public ResponseEntity<ApiResponse<?>> handleFieldException(MandatoryFieldException e){
        return ResponseEntity.badRequest().body(new ApiResponse<>("20", e.getMessage(),null));
    }
    
    @ExceptionHandler(FormatException.class)
    public ResponseEntity<ApiResponse<?>> handleFormat(FormatException e){
        return ResponseEntity.badRequest().body(new ApiResponse<>("21", e.getMessage(), null));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAll(Exception e){
        return ResponseEntity.internalServerError().body(new ApiResponse<>("90", "Service error: " + e.getMessage(), null));
    }
}
