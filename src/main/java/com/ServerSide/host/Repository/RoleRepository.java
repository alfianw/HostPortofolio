/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ServerSide.host.Repository;

import java.util.Optional;
import com.ServerSide.host.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hp
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByRole(String role);
}
