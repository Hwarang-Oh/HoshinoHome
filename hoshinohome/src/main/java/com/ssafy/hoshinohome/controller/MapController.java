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
import com.ssafy.hoshinohome.model.service.MapService;
import com.ssafy.hoshinohome.model.vo.HouseDealVo;
import com.ssafy.hoshinohome.model.vo.InputRangeVo;

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

    @GetMapping("/aptList{aptCode}")
    public ResponseEntity<List<HouseDeal>> getAptList(@PathVariable("aptCode") String aptCode) throws Exception {
        Long apt_code = Long.parseLong(aptCode);
        List<HouseDeal> HouseDeaList = mapService.getHouseDealList(apt_code);
        if (HouseDeaList != null) {
            return ResponseEntity.ok(HouseDeaList);
        } else
            return ResponseEntity.noContent().build();
    }
}
