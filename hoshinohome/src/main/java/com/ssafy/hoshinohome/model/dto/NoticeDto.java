package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDto {
    private String post_id; // PK
    private String title;
    private String content;
    private String date;

    public NoticeDto() {
    }

    public NoticeDto(String post_id, String title, String content, String date) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
