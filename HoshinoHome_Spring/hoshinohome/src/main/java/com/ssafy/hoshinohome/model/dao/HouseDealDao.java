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

        HouseDealVo selectEachHouseDealVo(@Param("houseCode") Long houseCode,
                        @Param("houseTypes") List<Integer> houseTypes,
                        @Param("dealTypes") List<Integer> dealTypes);

        List<HouseDealVo> selectHouseDealVoList2(@Param("lngFrom") String lngFrom,
                        @Param("lngTo") String lngTo,
                        @Param("latFrom") String latFrom,
                        @Param("latTo") String latTo,
                        @Param("houseTypes") List<Integer> houseTypes,
                        @Param("dealTypes") List<Integer> dealTypes);

        List<HouseDeal> selectHouseDealList(@Param("houseCode") Long houseCode);
}
