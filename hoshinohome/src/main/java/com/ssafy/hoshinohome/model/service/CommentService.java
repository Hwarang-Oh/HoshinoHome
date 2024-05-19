package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dto.Comment;

import java.util.List;

public interface CommentService {
    boolean addComment(Comment comment);

    boolean modifyComment(Comment comment);

    boolean removeComment(int comment_id);

    List<Comment> getCommentsByPostAndBoard(int post_id, String board_type);
}
