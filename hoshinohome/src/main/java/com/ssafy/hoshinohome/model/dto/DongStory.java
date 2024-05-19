package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class DongStory {
    private int post_id; // PK
    private String title;
    private String content;
    private int user_id; // FK
    private LocalDateTime date;
    private String region; // 지역 구분

    public DongStory() {}

    public DongStory(int post_id, String title, String content, int user_id, LocalDateTime date, String region) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.date = date;
        this.region = region;
    }
}
