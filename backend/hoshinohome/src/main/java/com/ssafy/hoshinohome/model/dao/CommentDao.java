package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao {
    int insertComment(Comment comment);

    int updateComment(Comment comment);

    int deleteComment(int comment_id);

    List<Comment> selectCommentsByPostAndBoard(@Param("post_id") int post_id, @Param("board_type") String board_type);
}
