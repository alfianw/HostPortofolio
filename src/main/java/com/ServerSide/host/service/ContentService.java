/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ServerSide.host.service;

import com.ServerSide.host.Repository.ContentRepository;
import com.ServerSide.host.Repository.UserRepository;
import com.ServerSide.host.dto.ApiResponse;
import com.ServerSide.host.dto.InsertContentRequest;
import com.ServerSide.host.exception.FailedException;
import com.ServerSide.host.exception.FormatException;
import com.ServerSide.host.exception.ResourceNotFoundException;
import com.ServerSide.host.models.Content;
import com.ServerSide.host.models.User;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hp
 */
@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    @Value("${file.contentImage-dir}")
    private String contentImage;

    public ApiResponse insertContent(InsertContentRequest request, String email) {

        request.setUserId(contentRepository.findIdByEmail(email));

        User user = userRepository.findById(Long.parseLong(request.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("Id Not Found"));

        contentRepository.findByImageContent(request.getImageContent().getOriginalFilename())
                .ifPresent(u -> {
                    throw new FailedException("Image allready exist");
                });

        contentRepository.findByContentTitle(request.getContentTitle())
                .ifPresent(u -> {
                    throw new FailedException("Title allready exist");
                });

        contentRepository.findByUrlContent(request.getContentUlr())
                .ifPresent(u -> {
                    throw new FailedException("Url allready exist");
                });

        if (request.getUserId().isBlank() || request.getUserId() == null) {
            throw new FormatException("Id cannot be empty");
        } else if (request.getImageContent().getOriginalFilename().isBlank() || request.getImageContent() == null) {
            throw new FormatException("Image cannot be empty");
        } else if (request.getContentTitle().isBlank() || request.getContentTitle() == null) {
            throw new FormatException("Tittle cannot be empty");
        }

        Content content = new Content();
        content.setContentTitle(request.getContentTitle());
        content.setContentDescription(request.getContentDescription());
        content.setUrlContent(request.getContentUlr());
        content.setLikes(0L);
        content.setUser(user);

        if (request.getImageContent() != null && !request.getImageContent().isEmpty()) {
            try {
                String userName = contentRepository.findUserNameById(Long.parseLong(request.getUserId()));
                String originalFilename = request.getImageContent().getOriginalFilename();
                String sanitizedFilename = originalFilename.replaceAll("\\s+", "_");
                String filename = userName + "_" + System.currentTimeMillis() + "_" + sanitizedFilename;
                File destination = new File(contentImage + filename);
                destination.getParentFile().mkdirs();
                request.getImageContent().transferTo(destination);

                content.setImageContent("/asset/content-images/" + filename);
            } catch (Exception e) {
                throw new FailedException("Failed to save image");
            }
        }

        contentRepository.save(content);

        return new ApiResponse("00", "Success insert Content", null);
    }

    public ApiResponse like(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));

        content.setLikes(content.getLikes() + 1);
        contentRepository.save(content);

        return new ApiResponse("00", "Success Like", null);
    }

    public ApiResponse unlike(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("cContent not found"));

        if (content.getLikes() > 0) {
            content.setLikes(content.getLikes() -1);
            contentRepository.save(content);
        }
        
        return new ApiResponse("00","Success Unlike", null);
    }

}
