package com.socialapp.service;

import com.socialapp.model.entity.User;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(Long id);
    void deleteByAdmin(Long id);
    void deleteSelf(User user);
}
