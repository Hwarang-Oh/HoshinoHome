package com.ssafy.hoshinohome.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Notice {
    private String post_id; // PK
    private String title;
    private String content;

    private LocalDateTime date;

}
