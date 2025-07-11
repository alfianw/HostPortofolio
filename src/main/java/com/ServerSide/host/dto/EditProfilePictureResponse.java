/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Hp
 */
@Data
@Builder
public class EditProfilePictureResponse {

    private String newProfileImagePath;
}
