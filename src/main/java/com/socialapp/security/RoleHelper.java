package com.socialapp.security;

import com.socialapp.model.entity.User;
import com.socialapp.model.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleHelper {
    public boolean isAdmin(User u) {
        return u != null && u.getRole() == Role.ADMIN;
    }
}
