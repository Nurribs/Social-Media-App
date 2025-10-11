package com.socialapp.dto;

public record CommentResponse(Long id, Long authorId, String authorUsername, String text, java.time.Instant createdAt) {

}
