package com.zhakypov.lesson.controller;


import com.zhakypov.lesson.dao.ILikedDao;
import com.zhakypov.lesson.model.ILiked;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.service.ILikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class ILikedController {
    private final ILikedService iLikedService;

    @GetMapping("/iLiked/{publication_id}")
    public ResponseEntity<?> getILiked(@PathVariable Long publication_id, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<String>(iLikedService.getILiked(user.getId(), publication_id), HttpStatus.OK);
    }

    @PostMapping("/iLiked/set/{publication_id}")
    public ResponseEntity<?> setILiked(@PathVariable Long publication_id, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<String>(iLikedService.setILiked(publication_id, user.getId()), HttpStatus.OK);
    }
}
