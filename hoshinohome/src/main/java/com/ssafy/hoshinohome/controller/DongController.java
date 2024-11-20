package com.ssafy.hoshinohome.controller;

import com.ssafy.hoshinohome.model.dto.Dong;
import com.ssafy.hoshinohome.model.service.DongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dong")
public class DongController {

    private final DongService dongService;

    @Autowired
    public DongController(DongService dongService) {
        this.dongService = dongService;
    }

    @GetMapping
    public ResponseEntity<List<Dong>> getDongList() {
        try {
            List<Dong> dongList = dongService.getAllDongs();
            return ResponseEntity.ok(dongList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
