package com.socialapp.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.socialapp.model.entity.User;
import com.socialapp.repository.TokenRepository;
import com.socialapp.repository.UserRepository;
import com.socialapp.service.UserService;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImplementation implements UserService {
    private final UserRepository users;
    private final TokenRepository tokens;

    public UserServiceImplementation(UserRepository users, TokenRepository tokens){
        this.users = users;
        this.tokens = tokens;
    }

    @Override
    @Transactional
    public Optional<User> getById(Long id){
        return users.findById(id);
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long id){
        tokens.deleteAllByUserId(id);
        users.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteSelf(User me){
        tokens.deleteAllByUserId(me.getId());
        users.deleteById(me.getId());
    }

}
