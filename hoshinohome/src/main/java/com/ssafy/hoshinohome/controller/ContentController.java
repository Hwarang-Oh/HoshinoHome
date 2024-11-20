package com.ssafy.hoshinohome.controller;

import com.ssafy.hoshinohome.util.GPTContentGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate")
@RequiredArgsConstructor
public class ContentController {

    private final GPTContentGenerator gptContentGenerator;

    @PostMapping("/dongstory")
    public ResponseEntity<String> generateDongStory(@RequestParam int count) throws Exception {
        gptContentGenerator.generateDongStoryContent(count);
        return ResponseEntity.ok("DongStory content generated successfully.");
    }

    @PostMapping("/notice")
    public ResponseEntity<String> generateNotice(@RequestParam int count) throws Exception {
        gptContentGenerator.generateNoticeContent(count);
        return ResponseEntity.ok("Notice content generated successfully.");
    }
}
