package com.zhakypov.lesson.service;

import com.zhakypov.lesson.dao.UserSubscriptionsDao;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.model.UserSubscriptions;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSubscriptionsService {
    private final UserSubscriptionsDao userSubscriptionsDao;

    public List<User> getSubscriptions(Long id){
        return userSubscriptionsDao.getSubscriptions(id);
    }

    public List<User> getSubscribers(Long id){
        return userSubscriptionsDao.getSubscribers(id);
    }

    public Boolean subscribedToUser(Long subscribedToUserId, Long user_id){
        return userSubscriptionsDao.subscribedToUser(subscribedToUserId,user_id);
    }
}
