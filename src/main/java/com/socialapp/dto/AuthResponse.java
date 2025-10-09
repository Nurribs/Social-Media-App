package com.socialapp.dto;

import java.time.Instant;

public record AuthResponse(String accessToken, Instant expiresAt)
{}
