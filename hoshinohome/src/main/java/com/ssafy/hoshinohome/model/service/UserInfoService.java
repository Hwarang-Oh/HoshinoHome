package com.ssafy.hoshinohome.model.service;

import java.util.List;

import com.ssafy.hoshinohome.model.dto.UserInfo;

public interface UserInfoService {

    boolean registerUserInfo(UserInfo userInfo) throws Exception;

    boolean modifyUserInfo(UserInfo userInfo) throws Exception;

    boolean removeUserInfo(String id) throws Exception;

    UserInfo getUserInfo(String id) throws Exception;

    String login(String id, String password) throws Exception;

    boolean removeFavoritePlace(String[] places) throws Exception;

    boolean registerFavoritePlace(String[] places) throws Exception;

    List<?> getFavoritePlace(String id) throws Exception;
}
