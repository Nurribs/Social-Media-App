package com.socialapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordReq(
        @NotBlank
        String currentPassword,
        @NotBlank @Size(min = 6, max = 72)
        String newPassword
) {}
