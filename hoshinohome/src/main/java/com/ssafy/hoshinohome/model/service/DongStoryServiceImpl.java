package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dao.DongStoryDao;
import com.ssafy.hoshinohome.model.dto.DongStory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DongStoryServiceImpl implements DongStoryService {

    private final DongStoryDao dongStoryDao;

    public DongStoryServiceImpl(DongStoryDao dongStoryDao) {
        this.dongStoryDao = dongStoryDao;
    }

    @Override
    public boolean addDongStory(DongStory dongStory) throws Exception {
        return dongStoryDao.insertDongStory(dongStory) > 0;
    }

    @Override
    public boolean modifyDongStory(DongStory dongStory) throws Exception {
        return dongStoryDao.updateDongStory(dongStory) > 0;
    }

    @Override
    public boolean removeDongStory(int post_id) throws Exception {
        return dongStoryDao.deleteDongStory(post_id) > 0;
    }

    @Override
    public DongStory getDongStory(int post_id) throws Exception {
        return dongStoryDao.selectDongStory(post_id);
    }

    @Override
    public List<DongStory> getDongStoriesByDongCode(String dong_code) throws Exception {
        return dongStoryDao.selectDongStoriesByDongCode(dong_code);
    }

    @Override
    public List<DongStory> getAllDongStories() throws Exception {
        return dongStoryDao.selectAllDongStories();
    }
}
