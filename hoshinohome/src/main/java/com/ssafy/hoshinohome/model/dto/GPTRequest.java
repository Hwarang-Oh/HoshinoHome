package com.ssafy.hoshinohome.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GPTRequest {

    private String model;
    private List<Message> messages;
    private int temperature;
    private int maxTokens;
    private int topP;
    private int frequencyPenalty;
    private int presencePenalty;

    public GPTRequest(String model
            , String prompt
            , int temperature
            , int maxTokens
            , int topP
            , int frequencyPenalty
            , int presencePenalty) {
        this.model = model;
        this.messages = new ArrayList<>();
        // 초기 메시지를 추가합니다.
        this.messages.add(new Message("system", "너는 부동산관련 전문가이자 최고의 아이돌 호시노 아이라는 사람이야. 이걸 항상 자랑하며 반말로 대답해줘! 그리고 항상 냥냥거리는 말투를 써! 이제 상황극 시작!"));
        this.messages.add(new Message("user",prompt));
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP = topP;
        this.frequencyPenalty = frequencyPenalty;
        this.presencePenalty = presencePenalty;
    }
}
