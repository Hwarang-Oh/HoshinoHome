package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DongStory {
    private int post_id; // PK
    private String dong_code;
    private String title;
    private String content;
    private LocalDateTime date;
    private int user_id;
    private String user_name;
    private String dong_name;
}
