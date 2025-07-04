/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    
    private String responseCode;
    private String responseMessage;
    private T data;
}
