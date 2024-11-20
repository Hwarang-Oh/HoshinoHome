package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseDeal {
    private Long deal_code; // PK
    private Integer deal_type;
    private String deal_amount;
    private String deposit_amount;
    private String monthly_amount;
    private String jibun_address;
    private String road_address;
    private String deal_year;
    private String deal_month;
    private String deal_day;
    private String area;
    private String floor;
    private Long house_code; // FK
    private Integer house_type;

    public HouseDeal() {
    }

    public HouseDeal(Long deal_code, Integer deal_type, String deal_amount, String deposit_amount,
            String monthly_amount, String jibun_address, String road_address, String deal_year, String deal_month,
            String deal_day, String area, String floor, Long house_code, Integer house_type) {
        this.deal_code = deal_code;
        this.deal_type = deal_type;
        this.deal_amount = deal_amount;
        this.deposit_amount = deposit_amount;
        this.monthly_amount = monthly_amount;
        this.jibun_address = jibun_address;
        this.road_address = road_address;
        this.deal_year = deal_year;
        this.deal_month = deal_month;
        this.deal_day = deal_day;
        this.area = area;
        this.floor = floor;
        this.house_code = house_code;
        this.house_type = house_type;
    }

}
