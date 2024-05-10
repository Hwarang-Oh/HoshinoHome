package com.ssafy.hoshinohome.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.ssafy.hoshinohome.model.dto.Notice;
import com.ssafy.hoshinohome.model.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// /dept 로 시작하는 모든 요청들은 로그인해야만 사용할수 있도록 인터셉터 처리하자!!
// 인터셉터에서는 로그인여부 체크해서 로그인 되었으면 계속해서 진행
//								 안되어있으면 로그인화면으로...

@RequestMapping("/api/notices")
@RestController
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Operation(summary = "모든 공지 조회")
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() throws Exception {
        List<Notice> notices = noticeService.getNotices();
        if (notices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notices);
    }

    @Operation(summary = "공지 등록")
    @PostMapping
    protected ResponseEntity<?> registerNotice(@RequestBody Notice notice) throws Exception{
        boolean result = noticeService.addNotice(notice);
        if(result) {
            return ResponseEntity.created(URI.create("/api/notices/" + notice.getPost_id())).build();
        }else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "공지 상세보기")
    @GetMapping("/{post_id}")
    protected ResponseEntity<?> getNoticeDetail(@PathVariable("post_id") String post_id) throws Exception{
        Notice notice = noticeService.getNotice(post_id);
        if(notice != null) {
            return ResponseEntity.ok(notice);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "공지 수정")
    @PutMapping("/{post_id}")
    protected ResponseEntity<?> modifyNotice(@RequestBody Notice notice) throws Exception{
        noticeService.modifyNotice(notice);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공지 삭제")
    @DeleteMapping("/{post_id}")
    protected ResponseEntity<?> removeNotice(@PathVariable("post_id") String post_id) throws Exception{
        if(noticeService.removeNotice(post_id)){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e){
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
