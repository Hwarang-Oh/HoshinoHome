package com.ssafy.hoshinohome.controller;

import com.ssafy.hoshinohome.model.dto.DongStory;
import com.ssafy.hoshinohome.model.service.DongStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dongstories")
public class DongStoryController {

    @Autowired
    private DongStoryService dongStoryService;

    @PostMapping
    public ResponseEntity<?> addDongStory(@RequestBody DongStory dongStory) throws Exception {
        dongStory.setDate(java.time.LocalDateTime.now());
        if (dongStoryService.addDongStory(dongStory)) {
            return ResponseEntity.ok("DongStory 생성 성공!!");
        }
        return ResponseEntity.badRequest().body("DongStory 생성 실패!!");
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<?> updateDongStory(@PathVariable int post_id, @RequestBody DongStory dongStory) throws Exception {
        dongStory.setPost_id(post_id);
        dongStory.setDate(java.time.LocalDateTime.now());
        if (dongStoryService.modifyDongStory(dongStory)) {
            return ResponseEntity.ok("DongStory 수정 성공!!");
        }
        return ResponseEntity.badRequest().body("DongStory 수정 실패!!");
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<?> deleteDongStory(@PathVariable int post_id) throws Exception {
        if (dongStoryService.removeDongStory(post_id)) {
            return ResponseEntity.ok("DongStory 삭제 성공!!");
        }
        return ResponseEntity.badRequest().body("DongStory 삭제 실패!!");
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getDongStory(@PathVariable int post_id) throws Exception {
        DongStory dongStory = dongStoryService.getDongStory(post_id);
        if (dongStory != null) {
            return ResponseEntity.ok(dongStory);
        }
        return ResponseEntity.badRequest().body("DongStory 찾을 수 없음!!");
    }

    @GetMapping("/dong/{dong_code}")
    public ResponseEntity<?> getDongStoriesByDongCode(@PathVariable String dong_code) throws Exception {
        List<DongStory> dongStories = dongStoryService.getDongStoriesByDongCode(dong_code);
        if (dongStories != null) {
            return ResponseEntity.ok(dongStories);
        }
        return ResponseEntity.badRequest().body("DongStory 찾을 수 없음!!");
    }

    @GetMapping
    public ResponseEntity<?> getAllDongStories() throws Exception {
        List<DongStory> dongStories = dongStoryService.getAllDongStories();
        if (dongStories != null && !dongStories.isEmpty()) {
            return ResponseEntity.ok(dongStories);
        }
        return ResponseEntity.noContent().build();
    }
}
