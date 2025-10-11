package com.socialapp.controller;

import com.socialapp.dto.ChangePasswordReq;
import com.socialapp.dto.UserResponse;
import com.socialapp.model.entity.User;
import com.socialapp.repository.UserRepository;
import com.socialapp.security.RoleHelper;
import com.socialapp.service.AuthService;
import com.socialapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository users;
    private final UserService userService;
    private final AuthService authService;
    private final RoleHelper roles;

    public UserController(UserRepository users,
                          UserService userService,
                          AuthService authService,
                          RoleHelper roles) {
        this.users = users;
        this.userService = userService;
        this.authService = authService;
        this.roles = roles;
    }

    private User auth(HttpServletRequest req) {
        Object u = req.getAttribute("authUser");   // TokenAuth burada bırakıyor
        if (u == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "missing_token");
        return (User) u;
    }

    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id, HttpServletRequest req) {
        auth(req);
        User u = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user_not_found"));
        return UserResponse.from(u);
    }

    @Transactional
    @PutMapping("/users/me/password")
    public ResponseEntity<?> changeMyPassword(@Valid @RequestBody ChangePasswordReq body,
                                              HttpServletRequest req) {
        User me = auth(req);
        authService.changePassword(me, body.currentPassword(), body.newPassword());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/users/me")
    public ResponseEntity<Void> deleteMe(
            HttpServletRequest req,
            @RequestBody(required = false) String _ignoreBody // Gövde gelse bile yok say yoksa 500 ınternal hatası veriyor postman
    ) {
        User me = auth(req);
        userService.deleteSelf(me);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<?> deleteByAdmin(@PathVariable Long id, HttpServletRequest req) {
        User me = auth(req);
        if (!roles.isAdmin(me)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "admin_required");
        }
        // Admin kendi hesabını silmek isterse de izin veriyoruz ama isteğe göre bu yasaklanadabilir
        userService.deleteByAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
