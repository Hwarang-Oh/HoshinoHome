package com.ssafy.hoshinohome.controller;

import com.ssafy.hoshinohome.model.dto.Comment;
import com.ssafy.hoshinohome.model.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        comment.setDate(LocalDateTime.now());
        if (commentService.addComment(comment)) {
            return ResponseEntity.ok("Comment 생성 성공!!");
        }
        return ResponseEntity.badRequest().body("Comment 생성 실패!!");
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<?> updateComment(@PathVariable int comment_id, @RequestBody Comment comment) {
        comment.setComment_id(comment_id);
        comment.setDate(LocalDateTime.now());
        if (commentService.modifyComment(comment)) {
            return ResponseEntity.ok("Comment 수정 성공!!");
        }
        return ResponseEntity.badRequest().body("Comment 수정 실패!!");
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable int comment_id) {
        if (commentService.removeComment(comment_id)) {
            return ResponseEntity.ok("Comment 삭제 성공!!");
        }
        return ResponseEntity.badRequest().body("Comment 생성 실패!!");
    }

    @GetMapping("/post/{post_id}/board/{board_type}")
    public ResponseEntity<?> getCommentsByPostAndBoard(@PathVariable int post_id, @PathVariable String board_type) {
        List<Comment> comments = commentService.getCommentsByPostAndBoard(post_id, board_type);
        if (comments != null) {
            return ResponseEntity.ok(comments);
        }
        return ResponseEntity.badRequest().body("Comment 찾을수 없음!!");
    }
}
