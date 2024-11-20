package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.DongStory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongStoryDao {
    int insertDongStory(DongStory dongStory);
    int updateDongStory(DongStory dongStory);
    int deleteDongStory(int post_id);
    DongStory selectDongStory(int post_id);
    List<DongStory> selectDongStoriesByDongCode(@Param("dong_code") String dong_code);
    List<DongStory> selectAllDongStories();
}
