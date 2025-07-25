/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Hp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPaginationResponse {

    private Long id;
    private String userName;
    private String email;
    private String isActive;
    private String name;
    private String pathImageProfile;
    private List<String> roles;
}
