package com.ssafy.hoshinohome.controller;

import com.ssafy.hoshinohome.model.dto.UserInfo;
import com.ssafy.hoshinohome.model.service.UserInfoService;
import com.ssafy.hoshinohome.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
