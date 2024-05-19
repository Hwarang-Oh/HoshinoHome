package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.HouseInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseInfoDao {
    HouseInfo selectHouseInfo(@Param("houseCode") Long houseCode);

    List<HouseInfo> selectHouseInfoListByQuery(@Param("query") String query);
}
