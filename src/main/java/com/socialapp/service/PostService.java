package com.socialapp.service;

import com.socialapp.dto.*;
import com.socialapp.model.entity.*;
import com.socialapp.repository.PostRepository;
import com.socialapp.security.RoleHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class PostService {
    private final PostRepository posts;
    private final RoleHelper roles;

    public PostService(PostRepository posts, RoleHelper roles) {
        this.posts = posts;
        this.roles = roles;
    }

    @Transactional
    public PostResponse create(User author, PostCreateReq req) {
        Post p = Post.builder()
                .author(author)
                .imageUrl(req.imageUrl())
                .caption(req.caption())
                .build();
        posts.save(p);
        return toResponse(p, List.of());
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long id) {
        Post p = posts.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));
        return toResponse(p, p.getComments().stream().map(c ->
                new CommentResponse(c.getId(), c.getAuthor().getId(), c.getAuthor().getUsername(), c.getText(), c.getCreatedAt())
        ).toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> list() {
        return posts.findAll().stream().map(p ->
                toResponse(p, null) // yorumları liste endpointinde doldurmayalım
        ).toList();
    }

    @Transactional
    public PostResponse update(User actor, Long id, PostUpdateReq req) {
        Post p = posts.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));
        if (!p.getAuthor().getId().equals(actor.getId()) && !roles.isAdmin(actor)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "forbidden");
        }
        if (req.imageUrl() != null) p.setImageUrl(req.imageUrl());
        if (req.caption() != null)  p.setCaption(req.caption());
        return toResponse(p, null);
    }

    @Transactional
    public void delete(User actor, Long id) {
        Post p = posts.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));
        if (!p.getAuthor().getId().equals(actor.getId()) && !roles.isAdmin(actor)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "forbidden");
        }
        posts.delete(p);
    }

    @Transactional
    public void addView(Long id) {
        Post p = posts.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "post_not_found"));
        p.setViews(p.getViews() + 1);
    }

    /* helper */
    @Transactional
    private PostResponse toResponse(Post p, List<CommentResponse> comments) {
        List<CommentResponse> cmts = comments != null ? comments : List.of();
        return new PostResponse(
                p.getId(),
                p.getAuthor().getId(),
                p.getAuthor().getUsername(),
                p.getImageUrl(),
                p.getCaption(),
                p.getViews(),
                p.getLikes(),
                p.getCreatedAt(),
                p.getUpdatedAt(),
                cmts
        );
    }
}
