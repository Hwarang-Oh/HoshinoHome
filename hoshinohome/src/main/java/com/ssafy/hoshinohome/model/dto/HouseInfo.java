package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseInfo {

    private Long apt_code; // PK
    private Integer build_year;
    private String road_name;
    private String road_name_bonbun;
    private String road_name_bubun;
    private String road_name_seq;
    private String road_name_basement_code;
    private String road_name_code;
    private String dong;
    private String bonbun;
    private String bubun;
    private String si_gun_gu_code;
    private String eub_myeon_dong_code;
    private String dong_code; // FK
    private String land_code;
    private String apartment_name;
    private String jibun;
    private String lng;
    private String lat;

    public HouseInfo() {
    }

    public HouseInfo(Long apt_code, int build_year, String road_name, String road_name_bonbun,
            String road_name_bubun, String road_name_seq, String road_name_basement_code, String road_name_code,
            String dong, String bonbun, String bubun, String si_gun_gu_code, String eub_myeon_dong_code,
            String dong_code, String land_code, String apartment_name, String jibun, String lng, String lat) {
        this.apt_code = apt_code;
        this.build_year = build_year;
        this.road_name = road_name;
        this.road_name_bonbun = road_name_bonbun;
        this.road_name_bubun = road_name_bubun;
        this.road_name_seq = road_name_seq;
        this.road_name_basement_code = road_name_basement_code;
        this.road_name_code = road_name_code;
        this.dong = dong;
        this.bonbun = bonbun;
        this.bubun = bubun;
        this.si_gun_gu_code = si_gun_gu_code;
        this.eub_myeon_dong_code = eub_myeon_dong_code;
        this.dong_code = dong_code;
        this.land_code = land_code;
        this.apartment_name = apartment_name;
        this.jibun = jibun;
        this.lng = lng;
        this.lat = lat;
    }

}
