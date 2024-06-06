package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dao.NoticeDao;
import com.ssafy.hoshinohome.model.dto.Notice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class NoticeServiceImpl implements NoticeService {

    private NoticeDao noticeDao;

    public NoticeServiceImpl(NoticeDao noticeDao) {
        this.noticeDao = noticeDao;
    }


    @Override
    public boolean addNotice(Notice notice) throws Exception {
        if (noticeDao.selectNotice(notice.getPost_id()) == null) {
            return noticeDao.insertNotice(notice)>0;
        }
        return false;
    }

    @Override
    public boolean modifyNotice(Notice notice) throws Exception {
        return noticeDao.updateNotice(notice)>0;
    }

    @Override
    public boolean removeNotice(String id) throws Exception{
        return noticeDao.deleteNotice(id)>0;
    }

    @Transactional(readOnly = true)
    @Override
    public Notice getNotice(String id) throws Exception{
        return noticeDao.selectNotice(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Notice> getNotices() throws Exception{
        return noticeDao.selectAllNotices();
    }
}
