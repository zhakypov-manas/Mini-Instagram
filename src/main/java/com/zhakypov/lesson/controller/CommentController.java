package com.zhakypov.lesson.controller;

import com.zhakypov.lesson.dao.CommentDao;
import com.zhakypov.lesson.dto.CommentDto;
import com.zhakypov.lesson.model.Comment;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class CommentController {
    private final CommentsService commentsService;

    @GetMapping("/comments/publication/{id}")
    public ResponseEntity<?> getAllCommentsInPublication(@PathVariable Long id){
        return new ResponseEntity<>(commentsService.getAllCommentInPublication(id), HttpStatus.OK);
    }

    @GetMapping("/comments/user/{id}")
    public ResponseEntity<?> getUserComments(@PathVariable Long id){
        return new ResponseEntity<List<Comment>>(commentsService.getUserComments(id), HttpStatus.OK);
    }

    @PostMapping("/comments/add")
    public ResponseEntity<?> addUserComments(@RequestBody CommentDto commentDto, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<String>(commentsService.addComment(commentDto,commentDto.getPublication_id(), user.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/comment/create")
    public ResponseEntity<?> addUserComments(@RequestBody CommentDto commentDto){
        return new ResponseEntity<CommentDto>(commentsService.createComment(commentDto), HttpStatus.OK);
    }

    @PostMapping("/comment/pub/{publication_id}/delete/{comment_id}")
    public ResponseEntity<?> deleteCommentInMyPost(@PathVariable Long publication_id,@PathVariable Long comment_id, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<Boolean>(commentsService.deleteCommentInMyPost(comment_id,publication_id,user.getEmail()), HttpStatus.OK);
    }
}
