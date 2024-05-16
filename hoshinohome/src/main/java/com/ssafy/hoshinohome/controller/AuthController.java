package com.ssafy.hoshinohome.controller;

import com.ssafy.hoshinohome.model.dto.UserInfo;
import com.ssafy.hoshinohome.model.service.UserInfoService;
import com.ssafy.hoshinohome.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserInfo user) {
        if (userInfoService.registerUser(user)) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.badRequest().body("Unable to register user");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserInfo loginUser) {
        UserInfo user = userInfoService.getUserByUsername(loginUser.getUser_name());
        if (user != null && passwordEncoder.matches(loginUser.getUser_password(), user.getUser_password())) {
            String token = jwtUtil.generateToken(user.getUser_name());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        UserInfo user = userInfoService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String token, @RequestBody UserInfo updatedUser) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        UserInfo user = userInfoService.getUserByUsername(username);
        if (user != null) {
            user.setUser_address(updatedUser.getUser_address());
            user.setUser_favorite_place(updatedUser.getUser_favorite_place());
            user.setUser_type(updatedUser.getUser_type());
            userInfoService.updateUser(user);
            return ResponseEntity.ok("User info updated successfully");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUserInfo(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        UserInfo user = userInfoService.getUserByUsername(username);
        if (user != null) {
            userInfoService.deleteUser(username);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}
