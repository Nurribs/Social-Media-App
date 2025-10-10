package com.socialapp.dto;

import com.socialapp.model.entity.User;
import com.socialapp.model.enums.Role;

public record UserResponse(
        Long id,
        String username,
        Role role
) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getRole());
    }
}
