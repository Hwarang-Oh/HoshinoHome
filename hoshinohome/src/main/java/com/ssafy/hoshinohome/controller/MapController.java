package com.ssafy.hoshinohome.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.hoshinohome.model.dto.HouseDeal;
import com.ssafy.hoshinohome.model.dto.HouseInfo;
import com.ssafy.hoshinohome.model.service.MapService;
import com.ssafy.hoshinohome.model.vo.EachInputDealVo;
import com.ssafy.hoshinohome.model.vo.ExtendedInputRangeVo;
import com.ssafy.hoshinohome.model.vo.HouseDealVo;
import com.ssafy.hoshinohome.model.vo.InputRangeVo;
import org.springframework.web.bind.annotation.RequestParam;

// /dept 로 시작하는 모든 요청들은 로그인해야만 사용할수 있도록 인터셉터 처리하자!!
// 인터셉터에서는 로그인여부 체크해서 로그인 되었으면 계속해서 진행
//								 안되어있으면 로그인화면으로...

@RequestMapping("/api/map")
@RestController
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @PostMapping("/searchedHouseDealVo")
    public ResponseEntity<HouseDealVo> getEachHouseDealVo(@RequestBody EachInputDealVo EachInputDealVo)
            throws Exception {
        HouseDealVo houseDealVo = mapService.getEachHouseDealVo(
                EachInputDealVo.getHouseCode(),
                EachInputDealVo.getHouseTypes(),
                EachInputDealVo.getDealTypes());
        if (houseDealVo != null) {
            return ResponseEntity.ok(houseDealVo);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/list")
    public ResponseEntity<List<HouseDealVo>> getHouseDealVoList(@RequestBody InputRangeVo InputRangeVo)
            throws Exception {
        List<HouseDealVo> houseDealVoList = mapService.getHouseDealVoList(
                InputRangeVo.getLngFrom(),
                InputRangeVo.getLngTo(),
                InputRangeVo.getLatFrom(),
                InputRangeVo.getLatTo());
        if (houseDealVoList != null) {
            return ResponseEntity.ok(houseDealVoList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/list2")
    public ResponseEntity<List<HouseDealVo>> getHouseDealVoList2(@RequestBody ExtendedInputRangeVo inputRangeVo)
            throws Exception {
        List<HouseDealVo> houseDealVoList2 = mapService.getHouseDealVoList2(
                inputRangeVo.getLngFrom(),
                inputRangeVo.getLngTo(),
                inputRangeVo.getLatFrom(),
                inputRangeVo.getLatTo(),
                inputRangeVo.getHouseTypes(),
                inputRangeVo.getDealTypes());
        if (houseDealVoList2 != null) {
            return ResponseEntity.ok(houseDealVoList2);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/dealList/{houseCode}")
    public ResponseEntity<List<HouseDeal>> getHouseDealList(@PathVariable("houseCode") String houseCode)
            throws Exception {
        Long house_code = Long.parseLong(houseCode);
        List<HouseDeal> HouseDeaList = mapService.getHouseDealList(house_code);
        if (HouseDeaList != null) {
            return ResponseEntity.ok(HouseDeaList);
        } else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/houseInfo/{houseCode}")
    public ResponseEntity<?> getHouseInfo(@PathVariable("houseCode") String houseCode) throws Exception {
        Long house_code = Long.parseLong(houseCode);
        HouseInfo houseInfo = mapService.getHouseInfo(house_code);
        if (houseInfo != null) {
            return ResponseEntity.ok(houseInfo);
        } else
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<HouseInfo> searchHouseInfoListByQuery(@RequestParam String query) throws Exception {
        return mapService.searchHouseInfoListByQuery(query);
    }

    @GetMapping("/search/{roadAddress}")
    public ResponseEntity<?> searchHouseInfoByAddress(@PathVariable("roadAddress") String roadAddress)
            throws Exception {
        HouseInfo houseInfo = mapService.searchHouseInfoByAddress(roadAddress);
        System.out.println(houseInfo);
        if (houseInfo != null) {
            return ResponseEntity.ok(houseInfo);
        } else
            return ResponseEntity.noContent().build();
    }

}
