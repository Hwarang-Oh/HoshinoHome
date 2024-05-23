package com.ssafy.hoshinohome.util;

import com.ssafy.hoshinohome.model.dto.DongStory;
import com.ssafy.hoshinohome.model.dto.GPTRequest;
import com.ssafy.hoshinohome.model.dto.GPTResponse;
import com.ssafy.hoshinohome.model.dto.Notice;
import com.ssafy.hoshinohome.model.dto.UserInfo;
import com.ssafy.hoshinohome.model.service.DongStoryService;
import com.ssafy.hoshinohome.model.service.NoticeService;
import com.ssafy.hoshinohome.model.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GPTContentGenerator {

    private final DongStoryService dongStoryService;
    private final NoticeService noticeService;
    private final UserInfoService userInfoService;
    private final RestTemplate restTemplate;

    @Value("${gpt.api.url}")
    private String apiUrl;
    @Value("${gpt.model}")
    private String model;

    // 샘플 데이터
    private final List<String> dongStoryTitles = List.of("동네 소개", "동네 명소", "동네 맛집", "동네 행사", "동네 역사");
    private final List<String> noticeTitles = List.of("공지사항", "이벤트 안내", "업데이트 공지", "긴급 공지", "점검 안내");

    // 동코드 리스트 생성
    private final List<String> dongCodes = createDongCodes();

    private static List<String> createDongCodes() {
        List<String> codes = new ArrayList<>();
        for (int i = 1111010100; i <= 1111018700; i += 100) {
            codes.add(String.valueOf(i));
        }
        return codes;
    }

    // 사용자 가져오기
    private UserInfo getRandomUser() {
        List<UserInfo> users = userInfoService.getAllUsers();
        return users.get(new Random().nextInt(users.size()));
    }

    // 랜덤 동코드 가져오기
    private String getRandomDongCode() {
        return dongCodes.get(new Random().nextInt(dongCodes.size()));
    }

    // GPT 요청 생성 함수
    private String generateContent(String prompt) {
        GPTRequest request = new GPTRequest(model, prompt, 1, 256, 1, 0, 0);
        request.addMessage("system", "짧게 대답해!");
        request.addMessage("user", prompt);

        GPTResponse response = restTemplate.postForObject(apiUrl, request, GPTResponse.class);
        return response.getChoices().get(0).getMessage().getContent();
    }

    private String truncateContent(String content, int maxLength) {
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength);
    }

    public void generateDongStoryContent(int count) throws Exception {
        for (int i = 0; i < count; i++) { // 요청된 수만큼 글 생성
            UserInfo user = getRandomUser();
            String title = dongStoryTitles.get(new Random().nextInt(dongStoryTitles.size()));
            String prompt = String.format("동네 이름: %s. %s에 대해 글을 작성해줘.", user.getUser_name(), title);
            String content = generateContent(prompt);

            // content 길이 자르기 (최대 10자로 제한)
            content = truncateContent(content, 10);

            DongStory dongStory = new DongStory();
            dongStory.setDong_code(getRandomDongCode()); // 랜덤 동코드 설정
            dongStory.setTitle(title);
            dongStory.setContent(content);
            dongStory.setDate(LocalDateTime.now());
            dongStory.setUser_id(user.getUser_id());

            dongStoryService.addDongStory(dongStory);
        }
    }

    public void generateNoticeContent(int count) throws Exception {
        for (int i = 0; i < count; i++) { // 요청된 수만큼 글 생성
            String title = noticeTitles.get(new Random().nextInt(noticeTitles.size()));
            String prompt = String.format("공지 제목: %s. %s에 대한 공지사항을 작성해줘.", title, title);
            String content = generateContent(prompt);

            // content 길이 자르기 (최대 10자로 제한)
            content = truncateContent(content, 10);

            Notice notice = new Notice();
            notice.setTitle(title);
            notice.setContent(content);
            notice.setDate(LocalDateTime.now());

            noticeService.addNotice(notice);
        }
    }
}
