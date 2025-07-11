/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service;

import com.ServerSide.host.Repository.UserRepository;
import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.ApiResponsePagination;
import com.ServerSide.host.dto.DetailProfileResponse;
import com.ServerSide.host.dto.EditProfilePictureResponse;
import com.ServerSide.host.dto.EditProfileRequest;
import com.ServerSide.host.dto.UserPaginationRequest;
import com.ServerSide.host.dto.UserPaginationResponse;
import com.ServerSide.host.exception.FailedException;
import com.ServerSide.host.exception.FormatException;
import com.ServerSide.host.exception.ResourceNotFoundException;
import com.ServerSide.host.models.Role;
import com.ServerSide.host.models.User;
import com.ServerSide.host.service.auth.UserSpecification;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    @Value("${file.upload-dir}")
    private String uploadPath;

    //pagination
    public ApiResponsePagination<List<UserPaginationResponse>> getUsersWithPagination(UserPaginationRequest request) {
        // parsing page & limit, konversi page agar dimulai dari 1
        int page = Integer.parseInt(request.getPage()) - 1;
        if (page < 0) {
            page = 0;
        }
        int limit = Integer.parseInt(request.getLimit());

        // sorting
        Sort.Direction direction = Sort.Direction.fromString(request.getSortOrder().toUpperCase());
        Sort sort = Sort.by(direction, request.getSortBy());

        Pageable pageable = PageRequest.of(page, limit, sort);

        // filtering
        Specification<User> spec = UserSpecification.getUsers(request.getFilters());

        Page<User> usersPage = userRepository.findAll(spec, pageable);

        if (usersPage.isEmpty()) {
            throw new ResourceNotFoundException("Data Not Found");
        }

        //user pagination
        List<UserPaginationResponse> userResponses = usersPage.getContent().stream()
                .map(user -> UserPaginationResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .name(user.getName())
                .pathImageProfile(user.getProfileImage())
                .roles(user.getRoles().stream()
                        .map(Role::getRole)
                        .collect(Collectors.toList()))
                .build())
                .collect(Collectors.toList());

        return ApiResponsePagination.<List<UserPaginationResponse>>builder()
                .responseCode("00")
                .responseMessage("Success")
                .totalPages(usersPage.getTotalPages())
                .currentPage(usersPage.getNumber() + 1)
                .totalData(usersPage.getTotalElements())
                .data(userResponses)
                .build();

    }
    //end user pagination

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
        String uploadDir = uploadPath;
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
        String fullPath = "/uploads/profile-images/" + fileName;
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
}
