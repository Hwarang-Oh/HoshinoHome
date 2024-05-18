package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dao.CommentDao;
import com.ssafy.hoshinohome.model.dto.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public boolean addComment(Comment comment) {
        return commentDao.insertComment(comment) > 0;
    }

    @Override
    public boolean modifyComment(Comment comment) {
        return commentDao.updateComment(comment) > 0;
    }

    @Override
    public boolean removeComment(int comment_id) {
        return commentDao.deleteComment(comment_id) > 0;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByPostAndBoard(int post_id, String board_type) {
        return commentDao.selectCommentsByPostAndBoard(post_id, board_type);
    }
}
