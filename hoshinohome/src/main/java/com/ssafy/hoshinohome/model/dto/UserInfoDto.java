package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {
    private String user_id; // PK
    private String user_pwd;
    private String user_name;
    private String user_address;
    private String user_phone;
    private String[] user_favorite_place; // HOW?

    public UserInfoDto() {
    }

    public UserInfoDto(String user_id, String user_pwd, String user_name, String user_address, String user_phone,
            String[] user_favorite_place) {
        this.user_id = user_id;
        this.user_pwd = user_pwd;
        this.user_name = user_name;
        this.user_address = user_address;
        this.user_phone = user_phone;
        this.user_favorite_place = user_favorite_place;
    }

}
