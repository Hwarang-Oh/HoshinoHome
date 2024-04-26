package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EviromentDto {
    private String dong_code; // PK , FK
    private int Station_proximity;
    private int commercial_area;
    private int Academic_num;
    private int Security_index;
    private int dong_score;

    public EviromentDto() {
    }

    public EviromentDto(String dong_code, int station_proximity, int commercial_area, int academic_num,
            int security_index, int dong_score) {
        this.dong_code = dong_code;
        Station_proximity = station_proximity;
        this.commercial_area = commercial_area;
        Academic_num = academic_num;
        Security_index = security_index;
        this.dong_score = dong_score;
    }

}
