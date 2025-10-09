package com.socialapp.service;

import com.socialapp.model.entity.User;
import com.socialapp.dto.AuthResponse;
import com.socialapp.dto.LoginReq;
import com.socialapp.dto.SignupReq;

public interface AuthService {
    User signup(SignupReq req);
    AuthResponse login(LoginReq req);
    void logout(String token);
    User me(String token);
    void changePassword(User user, String oldPass, String newPass);
}
