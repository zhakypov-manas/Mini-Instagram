package com.zhakypov.lesson.service;

import com.zhakypov.lesson.dao.ILikedDao;
import com.zhakypov.lesson.model.ILiked;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ILikedService {
    private final ILikedDao iLikedDao;

    public String getILiked(Long id, Long publication_id){
        return iLikedDao.getILiked(id, publication_id);
    }

    public String setILiked(Long publication_id, Long user_id){
        return iLikedDao.setILiked(publication_id, user_id);
    }
}
