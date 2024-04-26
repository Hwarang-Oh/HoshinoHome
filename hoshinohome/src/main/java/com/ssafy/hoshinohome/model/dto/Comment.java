package com.ssafy.hoshinohome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
    private int comment_id; // pk
    private int post_id; // fk
    private String user_id; // fk
    private String content;
    private String date;

    public Comment() {
    }

    public Comment(int comment_id, int post_id, String user_id, String content, String date) {
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.content = content;
        this.date = date;
    }

}
