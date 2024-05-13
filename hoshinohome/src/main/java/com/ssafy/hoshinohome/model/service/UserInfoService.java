package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dto.UserInfo;

public interface UserInfoService {

    String login(UserInfo userInfo);

    boolean register(UserInfo userInfo);
}
