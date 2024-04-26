package com.ssafy.hoshinohome.model.service;

import java.util.List;

import com.ssafy.hoshinohome.model.dto.UserInfo;

public class UserInfoServiceImpl implements UserInfoService {

    @Override
    public boolean registerUserInfo(UserInfo userInfo) throws Exception {
        return true;
    }

    @Override
    public boolean modifyUserInfo(UserInfo userInfo) throws Exception {
        return true;
    }

    @Override
    public boolean removeUserInfo(String id) throws Exception {
        return true;
    }

    @Override
    public UserInfo getUserInfo(String id) throws Exception {
        return null;
    }

    @Override
    public String login(String id, String password) throws Exception {
        return null;
    }

    @Override
    public boolean removeFavoritePlace(String[] places) throws Exception {
        return true;
    }

    @Override
    public boolean registerFavoritePlace(String[] places) throws Exception {
        return true;
    }

    @Override
    public List<?> getFavoritePlace(String id) throws Exception {
        return null;
    }

}
