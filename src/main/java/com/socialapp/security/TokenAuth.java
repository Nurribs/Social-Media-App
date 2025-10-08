package com.socialapp.security;

import java.time.Instant;
import com.socialapp.model.entity.Token;
import com.socialapp.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;


public class TokenAuth implements HandlerInterceptor{
    private final TokenRepository tokens;

    public TokenAuth(TokenRepository tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
        throws Exception {
        String tokenValue = req.getHeader("Access-Token");
        if (tokenValue == null || tokenValue.isBlank()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "missing_token");
            return false;
        }
        Token t = tokens.findByToken(tokenValue).orElse(null);
        if (t == null || !t.isActive(Instant.now())) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid or expired token");
            return false;
        }
        req.setAttribute("authUser", t.getUser());
        return true;
    }
}
