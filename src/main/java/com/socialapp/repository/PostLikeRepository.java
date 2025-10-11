package com.socialapp.repository;

import com.socialapp.model.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
    int deleteByPostIdAndUserId(Long postId, Long userId);
    int deleteByUserId(Long userId);
    List<Long> findDistinctPostIdByUserId(Long userId);

    //@Query("select distinct pl.post.id from PostLike pl where pl.user.id = :userId")
    //List<Long> findDistinctPostIdByUserId(@Param("userId") Long userId);
}
