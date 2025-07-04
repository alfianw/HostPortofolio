/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
@AllArgsConstructor
public class UserPaginationResponse {

    private Long id;
    private String userName;
    private String email;
    private String password;
    private String isActive;
    private String name;
    private List<String> roles;
}
