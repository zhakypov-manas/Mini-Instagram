package com.zhakypov.lesson.controller;

import com.zhakypov.lesson.dao.UserSubscriptionsDao;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.service.UserSubscriptionsService;
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
public class UserSubscriptionsController {
    private final UserSubscriptionsService userSubscriptionsService;

    @GetMapping("/userSubscriptions/")
    public ResponseEntity<?> getAllSubscriptions(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<List<User>>(userSubscriptionsService.getSubscriptions(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/userSubscribers/")
    public ResponseEntity<?> getAllSubscribers(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<List<User>>(userSubscriptionsService.getSubscribers(user.getId()), HttpStatus.OK);
    }

    @PostMapping("/userSubscribed/{id}")
    public ResponseEntity<?> subscribedToUser(@PathVariable Long id, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<Boolean>(userSubscriptionsService.subscribedToUser(id, user.getId()), HttpStatus.OK);
    }
}
