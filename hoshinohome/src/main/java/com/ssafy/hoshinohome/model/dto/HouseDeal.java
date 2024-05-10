package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseDeal {
    private long deal_code; // PK
    private String deal_amount;
    private String deal_year;
    private String deal_month;
    private String deal_day;
    private String area;
    private String floor;
    private long apt_code; // FK

    public HouseDeal() {
    }

    public HouseDeal(long deal_code, String deal_amount, String deal_year, String deal_month, String deal_day,
            String area, String floor, long apt_code) {
        this.deal_code = deal_code;
        this.deal_amount = deal_amount;
        this.deal_year = deal_year;
        this.deal_month = deal_month;
        this.deal_day = deal_day;
        this.area = area;
        this.floor = floor;
        this.apt_code = apt_code;
    }

}
