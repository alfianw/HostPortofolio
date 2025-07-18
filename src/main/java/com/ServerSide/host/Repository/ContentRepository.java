/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ServerSide.host.Repository;

import com.ServerSide.host.models.Content;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hp
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Optional<Content> findByImageContent(String imageContent);

    Optional<Content> findByContentTitle(String contentTitle);

    Optional<Content> findByUrlContent(String urlContent);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    String findIdByEmail(@Param("email") String email);

    @Query("SELECT u.userName FROM User u WHERE u.id = :id")
    String findUserNameById(@Param("id") Long id);
}
