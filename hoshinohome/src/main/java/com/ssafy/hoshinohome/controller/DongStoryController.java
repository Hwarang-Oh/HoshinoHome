package com.ssafy.hoshinohome.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// /dept 로 시작하는 모든 요청들은 로그인해야만 사용할수 있도록 인터셉터 처리하자!!
// 인터셉터에서는 로그인여부 체크해서 로그인 되었으면 계속해서 진행
//								 안되어있으면 로그인화면으로...

@RequestMapping("/DongStory")
@RestController
public class DongStoryController {

}
