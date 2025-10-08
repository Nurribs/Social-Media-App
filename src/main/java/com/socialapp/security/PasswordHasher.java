package com.socialapp.security;

public interface PasswordHasher {
    String hash(String raw);
    boolean verify(String raw, String hash);
}
