/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service;

import com.ServerSide.host.Repository.UserRepository;
import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.ChangePasswordRequest;
import com.ServerSide.host.dto.DetailProfileResponse;
import com.ServerSide.host.dto.EditProfilePictureResponse;
import com.ServerSide.host.dto.EditProfileRequest;
import com.ServerSide.host.exception.FailedException;
import com.ServerSide.host.exception.FormatException;
import com.ServerSide.host.exception.ResourceNotFoundException;
import com.ServerSide.host.models.User;
import com.ServerSide.host.security.SecurityConfig;
import java.io.File;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Hp
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SecurityConfig SecurityConfig;

    @Value("${file.profileImage-dir}")
    private String profileImage;

    //detail profile
    public ApiResponse<DetailProfileResponse> getDetailProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        DetailProfileResponse detail = DetailProfileResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .name(user.getName())
                .isActive(user.getIsActive())
                .pathImageProfile(user.getProfileImage())
                .build();

        return ApiResponse.<DetailProfileResponse>builder()
                .responseCode("00")
                .responseMessage("Success")
                .data(detail)
                .build();
    }
    //end detail profile

    //Edit poto profile
    public ApiResponse<EditProfilePictureResponse> updateProfilePicture(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email:" + email));

        String userName = user.getUserName();
        String uploadDir = profileImage;
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String sanitizedFilename = originalFilename.replaceAll("\\s+", "_");
        String fileName = userName + "_" + System.currentTimeMillis() + sanitizedFilename;

        try {
            File destinationFile = new File(uploadDir + File.separator + fileName);
            file.transferTo(destinationFile);
        } catch (Exception e) {
            throw new FailedException("Failed to upload image");
        }
        String fullPath = "/asset/profile-images/" + fileName;
        user.setProfileImage(fullPath);
        userRepository.save(user);

        EditProfilePictureResponse detail = EditProfilePictureResponse.builder()
                .newProfileImagePath(fullPath)
                .build();

        return ApiResponse.<EditProfilePictureResponse>builder()
                .responseCode("00")
                .responseMessage("Success")
                .data(detail)
                .build();
    }
    //end foto profile

    //edit data user
    public ApiResponse<DetailProfileResponse> editDataUser(EditProfileRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        if (!request.getEmail().equalsIgnoreCase(email)) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new FormatException("Email already in use by another account");
            }
        }

        // Cek apakah username ingin diubah dan sudah dipakai user lain
        if (!user.getUserName().equals(request.getUserName())) {
            boolean usernameExists = userRepository.existsByUserName(request.getUserName());
            if (usernameExists) {
                throw new FormatException("Username already exists");
            }
            user.setUserName(request.getUserName());
        }

        // Update nama jika berubah
        if (!user.getName().equals(request.getName())) {
            user.setName(request.getName());
        }

        userRepository.save(user);

        DetailProfileResponse response = DetailProfileResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .userName(user.getUserName())
                .build();

        return ApiResponse.<DetailProfileResponse>builder()
                .responseCode("00")
                .responseMessage("User data updated successfully")
                .data(response)
                .build();
    }

    public ApiResponse<DetailProfileResponse> changePassword(ChangePasswordRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
                
        if (!SecurityConfig.passwordEncoder().matches(request.getCurrentPassword(), user.getPassword())) {
            throw new FormatException("Current password is incorrect");
        }
        
        user.setPassword(SecurityConfig.passwordEncoder().encode(request.getNewPassword()));
        userRepository.save(user);
        
        DetailProfileResponse response = DetailProfileResponse.builder()
                .email(user.getEmail())
                .build();
        
        return ApiResponse.<DetailProfileResponse>builder()
                .responseCode("00")
                .responseMessage("user Password update successfully")
                .data(response)
                .build();
    }
}
