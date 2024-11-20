package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseInfo {

    private Long house_code; // PK
    private Integer house_type;
    private Integer build_year;
    private String dong_code; // FK
    private String house_name;
    private String si_gun_gu_name;
    private String dong_name;
    private String road_name;
    private String bonbun;
    private String bubun;
    private String jibun_address;
    private String road_address;
    private String lng;
    private String lat;

    public HouseInfo() {
    }

    public HouseInfo(Long house_code, Integer house_type, Integer build_year, String dong_code, String house_name,
            String si_gun_gu_name, String dong_name, String road_name, String bonbun, String bubun,
            String jibun_address, String road_address, String lng, String lat) {
        this.house_code = house_code;
        this.house_type = house_type;
        this.build_year = build_year;
        this.dong_code = dong_code;
        this.house_name = house_name;
        this.si_gun_gu_name = si_gun_gu_name;
        this.dong_name = dong_name;
        this.road_name = road_name;
        this.bonbun = bonbun;
        this.bubun = bubun;
        this.jibun_address = jibun_address;
        this.road_address = road_address;
        this.lng = lng;
        this.lat = lat;
    }

}
