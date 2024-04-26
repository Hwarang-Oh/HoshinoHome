package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DongStory {
    private int post_id;
    private String user_id;
    private String dong_code;
    private String title;
    private String content;
    private String datetime;

    public DongStory() {
    }

    public DongStory(int post_id, String user_id, String dong_code, String title, String content, String datetime) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.dong_code = dong_code;
        this.title = title;
        this.content = content;
        this.datetime = datetime;
    }

}
