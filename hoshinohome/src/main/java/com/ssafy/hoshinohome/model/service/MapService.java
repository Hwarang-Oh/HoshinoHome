package com.ssafy.hoshinohome.model.service;

import java.util.List;

import com.ssafy.hoshinohome.model.dto.HouseDeal;
import com.ssafy.hoshinohome.model.dto.HouseInfo;
import com.ssafy.hoshinohome.model.vo.HouseDealVo;

public interface MapService {
        public List<HouseDealVo> getHouseDealVoList(String lngFrom, String lngTo, String latFrom, String latTo)
                        throws Exception;

        public List<HouseInfo> getHouseInfoList(String lngFrom, String lngTo, String latFrom, String latTo)
                        throws Exception;

        public List<HouseDeal> getHouseDealList(Long aptCode) throws Exception;
}
