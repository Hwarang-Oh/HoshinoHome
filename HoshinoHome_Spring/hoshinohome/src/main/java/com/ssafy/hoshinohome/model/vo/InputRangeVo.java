package com.ssafy.hoshinohome.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputRangeVo {
    String lngFrom;
    String lngTo;
    String latFrom;
    String latTo;

    public InputRangeVo() {
    }

    public InputRangeVo(String lngFrom, String lngTo, String latFrom, String latTo) {
        this.lngFrom = lngFrom;
        this.lngTo = lngTo;
        this.latFrom = latFrom;
        this.latTo = latTo;
    }
}
