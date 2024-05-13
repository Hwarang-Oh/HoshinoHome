package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dao.UserInfoDao;
import com.ssafy.hoshinohome.model.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public String login(UserInfo userInfo) {
        try {
            // DAO를 통해 로그인 시도
            String result = userInfoDao.login(userInfo);
            if (result != null) {
                return result; // 성공한 경우 결과 반환
            } else {
                return null; // 로그인 실패
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean register(UserInfo userInfo) {
        try {
            userInfoDao.register(userInfo);
            return true; // 등록 성공
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return false; // 등록 실패
        }
    }
}
