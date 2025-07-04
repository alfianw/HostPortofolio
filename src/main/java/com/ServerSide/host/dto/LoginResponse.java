/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Hp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    
    private String responseCode;
    private String responseMessage;
    private String token;
    private String userName;
    private String email;
}
