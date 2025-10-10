package com.socialapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupReq(
        @NotBlank
        @Size(min = 4, max = 30)
        String username,

        @NotBlank
        @Size(min = 6, max = 15)
        String password
) {}
