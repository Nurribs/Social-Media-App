package com.socialapp.web;

import com.socialapp.repository.TokenRepository;
import com.socialapp.security.TokenAuth;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    private final TokenRepository tokens;

    public WebConfig(TokenRepository tokens) {
        this.tokens = tokens;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenAuth(tokens))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**");
    }

}
