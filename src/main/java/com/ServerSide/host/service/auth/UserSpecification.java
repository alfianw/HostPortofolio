/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service.auth;

import com.ServerSide.host.dto.UserPaginationRequest;
import com.ServerSide.host.models.User;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Hp
 */
public class UserSpecification {

    public static Specification<User> getUsers(UserPaginationRequest.Filters filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters != null) {
                if (filters.getName() != null && !filters.getName().isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + filters.getName().toLowerCase() + "%"));
                }
                if (filters.getEmail() != null && !filters.getEmail().isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("email")), "%" + filters.getEmail().toLowerCase() + "%"));
                }
                if (filters.getUserName() != null && !filters.getUserName().isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("userName")), "%" + filters.getUserName().toLowerCase() + "%"));
                }
                if (filters.getIsActive() != null && !filters.getIsActive().isBlank()) {
                    predicates.add(cb.equal(cb.lower(root.get("isActive")), filters.getIsActive().toLowerCase()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
