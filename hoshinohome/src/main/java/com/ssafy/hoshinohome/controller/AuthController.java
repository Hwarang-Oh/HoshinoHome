package com.ssafy.hoshinohome.controller;

import com.ssafy.hoshinohome.model.dto.UserInfo;
import com.ssafy.hoshinohome.model.service.UserInfoService;
import com.ssafy.hoshinohome.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String kakaoClientId = "cb468ba340c66b67b05d3bf01aa83b7f";
    private final String kakaoRedirectUri = "http://127.0.0.1:8080/auth/kakao/callback";

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserInfo user) {
        UserInfo existingUser = userInfoService.getUserByUsername(user.getUser_name()); //중복아이디 있는지 확인
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
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

    @PostMapping("/me/favorite")
    public ResponseEntity<?> toggleFavoritePlace(@RequestHeader("Authorization") String token, @RequestBody Map<String, Long> payload) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        Long houseCode = payload.get("houseCode");
        if (houseCode == null) {
            return ResponseEntity.badRequest().body("houseCode is required");
        }
        if (userInfoService.toggleFavoritePlace(username, houseCode)) {
            UserInfo updatedUser = userInfoService.getUserByUsername(username);
            return ResponseEntity.ok(updatedUser.getUserFavoritePlaceSet());
        }
        return ResponseEntity.badRequest().body("Unable to toggle favorite place");
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<?> getFavoritePlaces(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        UserInfo user = userInfoService.getUserByUsername(username);
        if (user != null) {
            Set<Long> favoritePlaces = user.getUserFavoritePlaceSet();
            return ResponseEntity.ok(favoritePlaces);
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }






    @GetMapping("/kakao/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + kakaoClientId +
                "&redirect_uri=" + kakaoRedirectUri +
                "&response_type=code";
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        // 1. 인가 코드를 이용하여 액세스 토큰 요청
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get access token");
        }

        String accessToken = (String) response.getBody().get("access_token");

        // 2. 액세스 토큰을 이용하여 사용자 정보 요청
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);

        if (userInfoResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get user info");
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();
        System.out.println("UserInfo: " + userInfo);  // 응답 내용을 로그로 출력

        if (userInfo == null || !userInfo.containsKey("kakao_account")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid kakao account");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not provided");
        }

        // 3. 사용자 정보 저장 또는 업데이트
        UserInfo user = userInfoService.getUserByUsername(email);
        if (user == null) {
            user = new UserInfo();
            user.setUser_name(email);
            user.setUser_password(passwordEncoder.encode("kakao")); // 임의의 비밀번호 설정
            user.setUser_address("");
            user.setUser_favorite_place("[]");
            user.setUser_type("user"); // 일반 사용자로 설정
            userInfoService.registerUser(user);
        }

        // 4. JWT 발급
        String jwtToken = jwtUtil.generateToken(user.getUser_name());
        return ResponseEntity.ok(jwtToken);
    }
}
