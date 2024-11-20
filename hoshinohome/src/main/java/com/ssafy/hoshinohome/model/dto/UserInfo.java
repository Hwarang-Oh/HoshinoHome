package com.ssafy.hoshinohome.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserInfo {
    private int user_id; // PK
    private String user_name;
    private String user_password;
    private String user_address;
    private String user_favorite_place; // JSON 문자열로 저장
    private String user_type;

    public UserInfo() {
    }

    public UserInfo(int user_id, String user_name, String user_password, String user_address, String user_favorite_place, String user_type) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_address = user_address;
        this.user_favorite_place = user_favorite_place;
        this.user_type = user_type;
    }

    public Set<Long> getUserFavoritePlaceSet() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.user_favorite_place, new TypeReference<Set<Long>>() {});
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    public void setUserFavoritePlaceSet(Set<Long> userFavoritePlaceSet) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.user_favorite_place = objectMapper.writeValueAsString(userFavoritePlaceSet);
        } catch (IOException e) {
            this.user_favorite_place = "[]";
        }
    }
}
