/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
public class UserPaginationRequest {
    
    private String limit;
    private String page;
    private String sortBy;
    private String sortOrder;
    private Filters filters;
    
    @Data
    public static class Filters{
        private String name;
        private String email;
        private String userName;
        private String isActive;
    }
}
