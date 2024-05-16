package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private int user_id; // PK
    private String user_name;
    private String user_password;
    private String user_address;
    private String user_favorite_place;
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
}
