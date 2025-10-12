package com.socialapp.controller;

import com.socialapp.model.entity.User;
import com.socialapp.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {
    private final LikeService likes;
    public LikeController(LikeService likes) {
        this.likes = likes;
    }

    private User auth(HttpServletRequest req) {
        return (User) req.getAttribute("authUser");
    }

    @PostMapping
    public ResponseEntity<Void> like(@PathVariable Long postId, HttpServletRequest r) {
        likes.like(auth(r), postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlike(@PathVariable Long postId, HttpServletRequest r) {
        likes.unlike(auth(r), postId);
        return ResponseEntity.noContent().build();
    }
}
