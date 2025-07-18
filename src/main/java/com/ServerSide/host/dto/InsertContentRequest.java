/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Hp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertContentRequest {
    
    private MultipartFile imageContent;
    
    private String contentTitle;
    
    private String contentDescription;
    
    private String userId;
    
    private String contentUlr;
    
}
