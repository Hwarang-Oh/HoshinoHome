package com.ssafy.hoshinohome;

import com.ssafy.hoshinohome.model.dao.UserInfoDao;
import com.ssafy.hoshinohome.model.dto.UserInfo;
import com.ssafy.hoshinohome.model.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserInfoServiceTest {

    @Mock
    private UserInfoDao userInfoDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserInfoService userInfoService;

    private UserInfo user;

    @BeforeEach
    void setUp() {
        user = new UserInfo(1, "user", "password", "address", "favorite_place", "type");
    }

    @Test
    void getUserByUsername_success() {
        given(userInfoDao.findByUsername("user")).willReturn(user);
        UserInfo found = userInfoService.getUserByUsername("user");
        assertEquals("user", found.getUser_name());
    }

    @Test
    void registerUser_success() {
        String encodedPassword = "encodedPassword";
        given(passwordEncoder.encode("password")).willReturn(encodedPassword);
        willDoNothing().given(userInfoDao).insertUser(any(UserInfo.class));

        assertTrue(userInfoService.registerUser(user));
        assertEquals(encodedPassword, user.getUser_password());
        verify(userInfoDao).insertUser(any(UserInfo.class));
    }
}
