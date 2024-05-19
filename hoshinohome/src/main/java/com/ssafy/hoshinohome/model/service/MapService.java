package com.ssafy.hoshinohome.model.service;

import java.util.List;

import com.ssafy.hoshinohome.model.dto.HouseDeal;
import com.ssafy.hoshinohome.model.dto.HouseInfo;
import com.ssafy.hoshinohome.model.vo.HouseDealVo;

public interface MapService {
        public List<HouseDealVo> getHouseDealVoList(String lngFrom, String lngTo, String latFrom, String latTo)
                        throws Exception;

        public List<HouseDealVo> getHouseDealVoList2(String lngFrom, String lngTo, String latFrom, String latTo,
                        List<Integer> houseTypes, List<Integer> dealTypes) throws Exception;

        public List<HouseDeal> getHouseDealList(Long houseCode) throws Exception;

        public HouseInfo getHouseInfo(Long houseCode) throws Exception;

        public List<HouseInfo> searchHouseInfoListByQuery(String query) throws Exception;

}
