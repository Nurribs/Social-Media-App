package com.socialapp.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasherBcrypt implements PasswordHasher {
    @Override
    public String hash(String raw) {
        return BCrypt.withDefaults().hashToString(12, raw.toCharArray());
    }

    @Override
    public boolean verify(String raw, String hash){
        return BCrypt.verifyer().verify(raw.toCharArray(), hash).verified;
    }
}
