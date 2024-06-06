package com.ssafy.hoshinohome.model.dao;

import com.ssafy.hoshinohome.model.dto.Dong;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DongDao {
    List<Dong> selectAllDongs();
}
