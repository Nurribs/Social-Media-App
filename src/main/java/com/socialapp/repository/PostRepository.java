package com.socialapp.repository;

import com.socialapp.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    int deleteByAuthorId(Long authorId);
}
