package com.socialapp.security;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UuidTokenGenerator implements TokenGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID() + "." + UUID.randomUUID();
    }
}
