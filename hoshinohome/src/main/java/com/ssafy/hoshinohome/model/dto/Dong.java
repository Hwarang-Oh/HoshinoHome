package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Dong {
    private String dong_code; // PK
    private String si_do_name;
    private String gu_gun_name;
    private String dong_name;
}
