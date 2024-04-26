package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeDao {

    int insertNotice(Notice notice);

    int updateNotice(Notice notice);

    int deleteNotice(String id);

    Notice selectNotice(String id);

    List<Notice> selectAllNotices();

}
