/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service;

import com.ServerSide.host.Repository.UserRepository;
import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.ApiResponsePagination;
import com.ServerSide.host.dto.UserPaginationRequest;
import com.ServerSide.host.dto.UserPaginationResponse;
import com.ServerSide.host.models.Role;
import com.ServerSide.host.models.User;
import com.ServerSide.host.service.auth.UserSpecification;
import java.util.Collections;
import java.util.List;
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
public class UserService {

    private final UserRepository userRepository;

 public ApiResponsePagination<List<UserPaginationResponse>> getUsersWithPagination(UserPaginationRequest request) {
        // parsing page & limit, konversi page agar dimulai dari 1
        int page = Integer.parseInt(request.getPage()) - 1;
        if (page < 0) page = 0;
        int limit = Integer.parseInt(request.getLimit());

        // sorting
        Sort.Direction direction = Sort.Direction.fromString(request.getSortOrder().toUpperCase());
        Sort sort = Sort.by(direction, request.getSortBy());

        Pageable pageable = PageRequest.of(page, limit, sort);

        // filtering
        Specification<User> spec = UserSpecification.getUsers(request.getFilters());

        // query paginasi
        Page<User> usersPage = userRepository.findAll(spec, pageable);

        // mapping entity ke DTO
        List<UserPaginationResponse> userResponses = usersPage.getContent().stream()
                .map(user -> UserPaginationResponse.builder()
                        .id(user.getId())
                        .userName(user.getUserName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .isActive(user.getIsActive())
                        .name(user.getName())
                        .roles(user.getRoles().stream()
                                .map(Role::getRole)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        // membangun response lengkap
        return ApiResponsePagination.<List<UserPaginationResponse>>builder()
                .responseCode("00")
                .responseMessage("Success")
                .totalPages(usersPage.getTotalPages())
                .currentPage(usersPage.getNumber() + 1) // 1-based index
                .totalData(usersPage.getTotalElements())
                .data(userResponses)
                .build();
    }

}
