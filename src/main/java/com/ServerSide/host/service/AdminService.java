/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service;

import com.ServerSide.host.Repository.RoleRepository;
import com.ServerSide.host.Repository.UserRepository;
import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.ApiResponsePagination;
import com.ServerSide.host.dto.DetailProfileResponse;
import com.ServerSide.host.dto.EditUserRoleRequest;
import com.ServerSide.host.dto.UserPaginationRequest;
import com.ServerSide.host.dto.UserPaginationResponse;
import com.ServerSide.host.exception.FormatException;
import com.ServerSide.host.exception.ResourceNotFoundException;
import com.ServerSide.host.models.Role;
import com.ServerSide.host.models.User;
import com.ServerSide.host.service.auth.UserSpecification;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hp
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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

    //delete users
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + id + " tidak ditemukan"));

        userRepository.delete(user);
    }
    //end delete

    //edit isActive
    @Transactional
    public ApiResponse<DetailProfileResponse> updateStatusAndRole(EditUserRoleRequest request) {

        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        //update isActive
        if (request.getIsActive() != null) {
            if (!request.getIsActive().equals("Y") && !request.getIsActive().equals("N")) {
                throw new FormatException("incorrect Value");
            }
            user.setIsActive(request.getIsActive());
        }

        userRepository.save(user);

        DetailProfileResponse detail = DetailProfileResponse.builder()
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .build();

        return ApiResponse.<DetailProfileResponse>builder()
                .responseCode("00")
                .responseMessage("Success Edit")
                .data(detail)
                .build();
    }
}
