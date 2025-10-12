package com.socialapp.service;

import com.socialapp.model.entity.Post;
import com.socialapp.model.entity.PostLike;
import com.socialapp.model.entity.User;
import com.socialapp.repository.PostLikeRepository;
import com.socialapp.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LikeService {
    private final PostRepository posts;
    private final PostLikeRepository likes;

    public LikeService(PostRepository posts, PostLikeRepository likes) {
        this.posts = posts;
        this.likes = likes;
    }

    @Transactional
    public void like(User user, Long postId) {
        Post p = posts.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post mevcut değil."));

        if (!likes.existsByPostIdAndUserId(postId, user.getId())) {
            likes.save(PostLike.builder().post(p).user(user).build());
            p.setLikesCount(likes.countByPostId(postId));
        }
    }


    @Transactional
    public void unlike(User user, Long postId) {
        Post p = posts.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post mevcut değil."));

        likes.deleteByPostIdAndUserId(postId, user.getId()); // idempotent
        p.setLikesCount(likes.countByPostId(postId));
    }
}
