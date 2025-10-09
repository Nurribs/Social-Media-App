package com.socialapp.bootstrap;

import com.socialapp.model.entity.User;
import com.socialapp.model.enums.Role;
import com.socialapp.repository.UserRepository;
import com.socialapp.security.PasswordHasher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner{
    private final UserRepository users;
    private final PasswordHasher hasher;

    public AdminSeeder(UserRepository users, PasswordHasher hasher){
        this.users= users;
        this.hasher=hasher;
    }

    @Override
    public void run(String... args){
        users.findByUsername("admin").orElseGet(() -> {
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(hasher.hash("admin123"))
                    .role(Role.ADMIN)
                    .build();
            return users.save(admin);
        });
    }
}
