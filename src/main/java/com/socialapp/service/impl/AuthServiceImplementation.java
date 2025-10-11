package com.socialapp.service.impl;

import com.socialapp.dto.AuthResponse;
import com.socialapp.dto.SignupReq;
import com.socialapp.dto.LoginReq;
import com.socialapp.model.entity.User;
import com.socialapp.model.entity.Token;
import com.socialapp.model.enums.Role;
import com.socialapp.repository.TokenRepository;
import com.socialapp.repository.UserRepository;
import com.socialapp.security.PasswordHasher;
import com.socialapp.security.TokenGenerator;
import com.socialapp.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class AuthServiceImplementation implements AuthService{
    private final UserRepository users;
    private final TokenRepository tokens;
    private final PasswordHasher hasher;
    private final TokenGenerator tokenGen;
    private final long expiryMin;  //minimum son kullanılma tarihi

    public AuthServiceImplementation(UserRepository users,TokenRepository tokens,PasswordHasher hasher,TokenGenerator tokenGen,
                                     @Value("${app.token.expiration-minutes:60}")
                                     long expiryMin) {
        this.users = users;
        this.tokens = tokens;
        this.hasher = hasher;
        this.tokenGen = tokenGen;
        this.expiryMin = expiryMin;
    }

    @Override
    @Transactional
    public User signup(SignupReq req) {
        if(users.existsByUsername(req.username())){
            throw new IllegalArgumentException("Kullanıcı adı mevcut.");
        }
        User u = User.builder()
                .username(req.username())
                .passwordHash(hasher.hash(req.password()))
                .role(Role.USER)
                .build();
        return users.save(u);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginReq req) {
        User u = users.findByUsername(req.username())
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz kimlik bilgileri. "));
        if (!hasher.verify(req.password(), u.getPasswordHash()))
            throw new IllegalArgumentException("Geçersiz kimlik bilgileri.");

        Token t = Token.builder()
                .user(u)
                .token(tokenGen.generate())
                .expiresAt(Instant.now().plus(expiryMin, ChronoUnit.MINUTES))
                .build();
        tokens.save(t);
        return new AuthResponse(t.getToken(), t.getExpiresAt());
    }

    @Override
    @Transactional
    public User me(String token) {
        Token t = tokens.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Token bulunmamadı."));
        if (!t.isActive(Instant.now())) {
            throw new IllegalArgumentException("Token aktif değil veya süresi doldu.");
        }
        return t.getUser();
    }

    @Override
    @Transactional
    public void logout(String token) {
        Token t = tokens.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Token Bulunamadı."));
        t.setRevokedAt(Instant.now());
        tokens.save(t);
    }

    @Override
    @Transactional
    public void changePassword(User user, String oldPass, String newPass){
        if (!hasher.verify(oldPass, user.getPasswordHash()))
            throw new IllegalArgumentException("Eski şifre yanlış.");
        user.setPasswordHash(hasher.hash(newPass));
        users.save(user);
    }
}
