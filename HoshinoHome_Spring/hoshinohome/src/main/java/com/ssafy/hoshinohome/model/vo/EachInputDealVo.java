package com.ssafy.hoshinohome.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EachInputDealVo {
    private Long houseCode;
    private List<Integer> houseTypes;
    private List<Integer> dealTypes;

    public EachInputDealVo() {
    }

    public EachInputDealVo(Long houseCode, List<Integer> houseTypes, List<Integer> dealTypes) {
        this.houseCode = houseCode;
        this.houseTypes = houseTypes;
        this.dealTypes = dealTypes;
    }
}
