package com.ssafy.hoshinohome.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.hoshinohome.model.dao.HouseDealDao;
import com.ssafy.hoshinohome.model.dao.HouseInfoDao;
import com.ssafy.hoshinohome.model.dto.HouseDeal;
import com.ssafy.hoshinohome.model.dto.HouseInfo;
import com.ssafy.hoshinohome.model.vo.HouseDealVo;

@Transactional(readOnly = true)
@Service
public class MapServiceImpl implements MapService {

    private final HouseDealDao houseDealDao;
    private final HouseInfoDao houseInfoDao;

    public MapServiceImpl(HouseDealDao houseDealDao, HouseInfoDao houseInfoDao) {
        this.houseDealDao = houseDealDao;
        this.houseInfoDao = houseInfoDao;
    }

    @Override
    public List<HouseDealVo> getHouseDealVoList(String lngFrom, String lngTo, String latFrom, String latTo)
            throws Exception {
        return houseDealDao.selectHouseDealVoList(lngFrom, lngTo, latFrom, latTo);
    }

    @Override
    public List<HouseInfo> getHouseInfoList(String lngFrom, String lngTo, String latFrom, String latTo)
            throws Exception {
        return houseInfoDao.selectHouseInfos(lngFrom, lngTo, latFrom, latTo);
    }

    @Override
    public List<HouseDeal> getHouseDealList(Long aptCode) throws Exception {
        return houseDealDao.selectHouseDeals(aptCode);
    }
}
