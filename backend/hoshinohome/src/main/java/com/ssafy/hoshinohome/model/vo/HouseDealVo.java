package com.ssafy.hoshinohome.model.vo;

import lombok.Getter;
import lombok.Setter;

/*
 * HouseDealVo : 지도를 움직일 때마다, 실거래가 존재하는 주소에 MarkUp을 찍어줌.
 * 이때 Map의 MarkUp에 찍힐 대표값을 HouseDealVo로 관리한다.
 */

@Getter
@Setter
public class HouseDealVo {
    private Long house_code; // VO의 PK
    private Integer house_type;
    private Integer deal_type;
    private String house_name;
    private String jibun_address;
    private String road_address;
    private String deal_amount;
    private String deposit_amount;
    private String monthly_amount;
    private String deal_year;
    private String deal_month;
    private String deal_day;
    private String area;
    private String floor;
    private String lng;
    private String lat;

    public HouseDealVo() {
    }

    public HouseDealVo(Long house_code, Integer house_type, Integer deal_type, String house_name, String jibun_address,
            String road_address, String deal_amount, String deposit_amount, String monthly_amount, String deal_year,
            String deal_month, String deal_day, String area, String floor, String lng, String lat) {
        this.house_code = house_code;
        this.house_type = house_type;
        this.deal_type = deal_type;
        this.house_name = house_name;
        this.jibun_address = jibun_address;
        this.road_address = road_address;
        this.deal_amount = deal_amount;
        this.deposit_amount = deposit_amount;
        this.monthly_amount = monthly_amount;
        this.deal_year = deal_year;
        this.deal_month = deal_month;
        this.deal_day = deal_day;
        this.area = area;
        this.floor = floor;
        this.lng = lng;
        this.lat = lat;
    }

}
