/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Hp
 */
@Data
public class RegisterForm {
    private String user_Name;
    private String email;
    private String password;
    private String name;
    private MultipartFile profileImage;
}
