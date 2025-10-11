package com.socialapp.repository;

import com.socialapp.model.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
    int deleteByPostIdAndUserId(Long postId, Long userId);
}
