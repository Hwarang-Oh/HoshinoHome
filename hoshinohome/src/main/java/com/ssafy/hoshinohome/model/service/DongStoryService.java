package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dto.DongStory;
import java.util.List;

public interface DongStoryService {
    boolean addDongStory(DongStory dongStory) throws Exception;
    boolean modifyDongStory(DongStory dongStory) throws Exception;
    boolean removeDongStory(int post_id) throws Exception;
    DongStory getDongStory(int post_id) throws Exception;
    List<DongStory> getDongStoriesByRegion(String region) throws Exception;
}
