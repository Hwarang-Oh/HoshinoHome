package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dao.DongDao;
import com.ssafy.hoshinohome.model.dto.Dong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DongServiceImpl implements DongService {

    private final DongDao dongDao;

    public DongServiceImpl(DongDao dongDao) {
        this.dongDao = dongDao;
    }

    @Override
    public List<Dong> getAllDongs() throws Exception {
        return dongDao.selectAllDongs();
    }
}
