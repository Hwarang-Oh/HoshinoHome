package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.HouseDeal;
import com.ssafy.hoshinohome.model.vo.HouseDealVo;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HouseDealDao {
    List<HouseDealVo> selectHouseDealVoList(@Param("lngFrom") String lngFrom,
            @Param("lngTo") String lngTo,
            @Param("latFrom") String latFrom,
            @Param("latTo") String latTo);

    List<HouseDeal> selectHouseDeals(@Param("aptCode") Long aptCode);
}
