package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.HouseInfo;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface HouseInfoDao {
    List<HouseInfo> selectHouseInfos(String lngFrom, String lngTo, String latFrom, String latTo);
}
