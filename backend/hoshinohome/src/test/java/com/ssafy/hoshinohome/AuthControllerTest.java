package com.ssafy.hoshinohome;

import com.ssafy.hoshinohome.controller.AuthController;
import com.ssafy.hoshinohome.model.dto.UserInfo;
import com.ssafy.hoshinohome.model.service.UserInfoService;
import com.ssafy.hoshinohome.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private UserInfo user;

    @BeforeEach
    void setUp() {
        user = new UserInfo(1, "user", "password", "address", "favorite_place", "type");
    }

    @Test
    void registerUser_success() {
        when(userInfoService.registerUser(any(UserInfo.class))).thenReturn(true);
        ResponseEntity<?> response = authController.registerUser(user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void loginUser_success() {
        when(userInfoService.getUserByUsername("user")).thenReturn(user);
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        when(jwtUtil.generateToken("user")).thenReturn("token");

        ResponseEntity<?> response = authController.loginUser(user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("token", response.getBody());
    }
}
