package com.socialapp.dto;

import java.time.Instant;
import java.util.List;

public record PostResponse(
        Long id,
        Long authorId,
        String authorUsername,
        String imageUrl,
        String caption,
        long views,
        long likes,
        Instant createdAt,
        Instant updatedAt,
        List<CommentResponse> comments
) {}
