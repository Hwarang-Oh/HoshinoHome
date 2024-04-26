package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseInfo {

    private Long apt_code; // PK
    private int build_year;
    private String road_name;
    private String road_name_main_num;
    private String road_name_sub_num;
    private String road_name_seq;
    private String road_name_basement_code;
    private String road_name_code;
    private String dong;
    private String main_num;
    private String sub_num;
    private String si_gun_gu_code;
    private String eub_myeon_dong_code;
    private String dong_code; // FK
    private String land_code;
    private String apartment_name;
    private String jibun;
    private String lon;
    private String lat;

    public HouseInfo() {
    }

    public HouseInfo(Long apt_code, int build_year, String road_name, String road_name_main_num,
                     String road_name_sub_num, String road_name_seq, String road_name_basement_code, String road_name_code,
                     String dong, String main_num, String sub_num, String si_gun_gu_code, String eub_myeon_dong_code,
                     String dong_code, String land_code, String apartment_name, String jibun, String lon, String lat) {
        this.apt_code = apt_code;
        this.build_year = build_year;
        this.road_name = road_name;
        this.road_name_main_num = road_name_main_num;
        this.road_name_sub_num = road_name_sub_num;
        this.road_name_seq = road_name_seq;
        this.road_name_basement_code = road_name_basement_code;
        this.road_name_code = road_name_code;
        this.dong = dong;
        this.main_num = main_num;
        this.sub_num = sub_num;
        this.si_gun_gu_code = si_gun_gu_code;
        this.eub_myeon_dong_code = eub_myeon_dong_code;
        this.dong_code = dong_code;
        this.land_code = land_code;
        this.apartment_name = apartment_name;
        this.jibun = jibun;
        this.lon = lon;
        this.lat = lat;
    }

}
