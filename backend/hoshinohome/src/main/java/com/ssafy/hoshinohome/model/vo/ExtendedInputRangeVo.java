package com.ssafy.hoshinohome.model.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtendedInputRangeVo extends InputRangeVo {
    private List<Integer> houseTypes;
    private List<Integer> dealTypes;

    // Getters and Setters

    public List<Integer> getHouseTypes() {
        return houseTypes;
    }

    public void setHouseTypes(List<Integer> houseTypes) {
        this.houseTypes = houseTypes;
    }

    public List<Integer> getDealTypes() {
        return dealTypes;
    }

    public void setDealTypes(List<Integer> dealTypes) {
        this.dealTypes = dealTypes;
    }
}
