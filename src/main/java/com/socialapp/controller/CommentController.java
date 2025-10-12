package com.socialapp.controller;

import com.socialapp.dto.CommentCreateReq;
import com.socialapp.dto.CommentResponse;
import com.socialapp.model.entity.User;
import com.socialapp.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService comments;
    public CommentController(CommentService comments) { this.comments = comments; }

    private User auth(HttpServletRequest req) { return (User) req.getAttribute("authUser"); }

    @PostMapping("/posts/{postId}/comments")
    public CommentResponse add(@PathVariable Long postId, @RequestBody CommentCreateReq req, HttpServletRequest r) {
        return comments.add(auth(r), postId, req);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> list(@PathVariable Long postId) {
        return comments.list(postId);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest r) {
        comments.delete(auth(r), id);
        return ResponseEntity.noContent().build();
    }
}
