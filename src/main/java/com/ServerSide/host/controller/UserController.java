/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.controller;

import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.ApiResponsePagination;
import com.ServerSide.host.dto.DetailProfileResponse;
import com.ServerSide.host.dto.EditProfilePictureRequest;
import com.ServerSide.host.dto.EditProfilePictureResponse;
import com.ServerSide.host.dto.EditProfileRequest;
import com.ServerSide.host.dto.RegisterForm;
import com.ServerSide.host.dto.UserPaginationRequest;
import com.ServerSide.host.dto.UserPaginationResponse;
import com.ServerSide.host.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Hp
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //pagination user
    @PostMapping("/userPagination")
    public ResponseEntity<ApiResponsePagination<List<UserPaginationResponse>>> getUsers(@RequestBody UserPaginationRequest request) {
        return ResponseEntity.ok(userService.getUsersWithPagination(request));
    }

    //detail user
    @GetMapping("/userDetail")
    public ResponseEntity<ApiResponse<DetailProfileResponse>> getDetailProfile(Authentication authentication) {
        // Ambil email dari JWT (sudah otomatis di-set oleh Spring Security)
        String email = authentication.getName();

        // Panggil service dan kembalikan response
        ApiResponse<DetailProfileResponse> response = userService.getDetailProfile(email);
        return ResponseEntity.ok(response);
    }

    //edit profile image
    @PutMapping("/update-foto-profile")
    public ResponseEntity<ApiResponse<EditProfilePictureResponse>> updateProfileImage(
            @ModelAttribute EditProfilePictureRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        MultipartFile image = request.getProfileImage();
        return ResponseEntity.ok(userService.updateProfilePicture(email, image));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<DetailProfileResponse>> editProfile(
            @RequestBody EditProfileRequest request,
            Authentication authentication) {
        
        String email = authentication.getName();
        return ResponseEntity.ok(userService.editDataUser(request, email));
    }
}
