package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Comment {
    private int comment_id; // PK
    private int post_id;  // FK
    private String board_type; // Notice와 DongStory 구분
    private int user_id;    // FK
    private String user_name;
    private String content;
    private LocalDateTime date;

    public Comment() {}

    public Comment(int comment_id, int post_id, String board_type, int user_id, String user_name, String content, LocalDateTime date) {
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.board_type = board_type;
        this.user_id = user_id;
        this.user_name = user_name;
        this.content = content;
        this.date = date;
    }
}
