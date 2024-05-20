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

    // Map 기능 1 : HouseDealListVoList를 가져온다. -> 대표값이 지도에 MarkUp으로 나타난다.
    @Override
    public List<HouseDealVo> getHouseDealVoList(String lngFrom, String lngTo, String latFrom, String latTo)
            throws Exception {
        return houseDealDao.selectHouseDealVoList(lngFrom, lngTo, latFrom, latTo);
    }

    @Override
    public List<HouseDealVo> getHouseDealVoList2(String lngFrom, String lngTo, String latFrom, String latTo,
            List<Integer> houseTypes, List<Integer> dealTypes) throws Exception {

        return houseDealDao.selectHouseDealVoList2(lngFrom, lngTo, latFrom, latTo, houseTypes, dealTypes);
    }

    // Map 기능 2 : HouseDealListVo를 통해 나타난 지도의 MarkUp을 선택하면, 해당 HouseCode를 가진 이전
    // 거래내역을
    // 가져온다.
    @Override
    public List<HouseDeal> getHouseDealList(Long houseCode) throws Exception {
        return houseDealDao.selectHouseDealList(houseCode);
    }

    // Map 기능 3 : HouseDealListVo를 통해 나타난 지도의 MarkUp을 선택하면, 해당 HouseCode를
    // 가진 House Info 대표 정보를 가져온다.
    @Override
    public HouseInfo getHouseInfo(Long houseCode) throws Exception {
        return houseInfoDao.selectHouseInfo(houseCode);
    }

    // Map 기능 4 : 건물명 혹은 지역 / 주소로 사용자는 입력한다. 해당 사용자의 입력에 대한 자동완성 기능을 제공한다.

    @Override
    public List<HouseInfo> searchHouseInfoListByQuery(String query) throws Exception {
        return houseInfoDao.selectHouseInfoListByQuery(query);
    }
}
