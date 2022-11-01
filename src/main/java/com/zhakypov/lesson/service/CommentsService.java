package com.zhakypov.lesson.service;

import com.zhakypov.lesson.dao.CommentDao;
import com.zhakypov.lesson.dto.CommentDto;
import com.zhakypov.lesson.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentDao commentDao;

    public List<CommentDto> getAllCommentInPublication(Long publication_id){
        return commentDao.getAllCommentInPublication(publication_id);
    }

    public List<Comment> getUserComments(Long id){
        return commentDao.getUserComments(id);
    }

    public String addComment(CommentDto commentDto, Long publication_id, String email){
        return commentDao.addComment(commentDto,publication_id,email);
    }

    public CommentDto createComment(CommentDto commentDto){
        return commentDao.createComment(commentDto);
    }

    public Boolean deleteCommentInMyPost(Long comment_id, Long publication_id, String email){
        return commentDao.deleteCommentInMyPost(comment_id,publication_id,email);
    }
}
