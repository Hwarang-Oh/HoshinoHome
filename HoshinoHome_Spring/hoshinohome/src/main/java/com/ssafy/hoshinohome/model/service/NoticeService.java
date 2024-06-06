package com.ssafy.hoshinohome.model.service;

import com.ssafy.hoshinohome.model.dto.Notice;

import java.util.List;

public interface NoticeService {
    boolean addNotice(Notice notice) throws Exception;

    boolean modifyNotice(Notice notice) throws Exception;

    boolean removeNotice(String id) throws Exception;

    Notice getNotice(String id) throws Exception;

    List<Notice> getNotices() throws Exception;
}
