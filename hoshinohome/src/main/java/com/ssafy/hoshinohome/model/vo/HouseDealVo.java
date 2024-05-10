package com.ssafy.hoshinohome.model.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseDealVo {
    private Long deal_code;
    private String deal_amount;
    private String deal_year;
    private String deal_month;
    private String deal_day;
    private String area;
    private String floor;
    private Long apt_code; // FK
    private String apartment_name;
    private String lng;
    private String lat;
    private String address;

    public HouseDealVo() {
    }

    public HouseDealVo(Long deal_code, String deal_amount, String deal_year, String deal_month, String deal_day,
            String area, String floor, Long apt_code, String apartment_name, String lng, String lat, String address) {
        this.deal_code = deal_code;
        this.deal_amount = deal_amount;
        this.deal_year = deal_year;
        this.deal_month = deal_month;
        this.deal_day = deal_day;
        this.area = area;
        this.floor = floor;
        this.apt_code = apt_code;
        this.apartment_name = apartment_name;
        this.lng = lng;
        this.lat = lat;
        this.address = address;
    }

}
