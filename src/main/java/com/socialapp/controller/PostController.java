package com.socialapp.controller;

import com.socialapp.dto.*;
import com.socialapp.model.entity.User;
import com.socialapp.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService posts;

    public PostController(PostService posts) { this.posts = posts; }

    private User auth(HttpServletRequest req) { return (User) req.getAttribute("authUser"); }

    @PostMapping
    public PostResponse create(@RequestBody PostCreateReq req, HttpServletRequest r) {
        return posts.create(auth(r), req);
    }

    @GetMapping("/{id}")
    public PostResponse get(@PathVariable Long id) { return posts.get(id); }

    @GetMapping
    public List<PostResponse> list() { return posts.list(); }

    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id, @RequestBody PostUpdateReq req, HttpServletRequest r) {
        return posts.update(auth(r), id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest r) {
        posts.delete(auth(r), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> addView(@PathVariable Long id) {
        posts.addView(id);
        return ResponseEntity.noContent().build();
    }
}
