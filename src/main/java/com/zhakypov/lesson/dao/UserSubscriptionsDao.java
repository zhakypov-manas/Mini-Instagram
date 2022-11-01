package com.zhakypov.lesson.dao;

import com.zhakypov.lesson.dto.UserSubscriptionsDto;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.model.UserSubscriptions;
import com.zhakypov.lesson.utility.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSubscriptionsDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public List<User> getSubscriptions(Long id){
        String sql = "select u.id,\n" +
                "       u.name,\n" +
                "       u.login,\n" +
                "       u.email,\n" +
                "       u.password,\n" +
                "       u.\"publicationsQuantity\",\n" +
                "       u.\"subscribersQuantity\",\n" +
                "       u.\"subscriptionsQuantity\"\n" +
                "from \"userSubscriptions\" as ust\n" +
                "inner join users u\n" +
                "on u.id = ust.\"subscribedToUser_id\"\n" +
                "where ust.user_id = ?;";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
        return users;
    }

    public List<User> getSubscribers(Long id){
        String sql = "select u.id,\n" +
                "       u.name,\n" +
                "       u.login,\n" +
                "       u.email,\n" +
                "       u.password,\n" +
                "       u.\"publicationsQuantity\",\n" +
                "       u.\"subscribersQuantity\",\n" +
                "       u.\"subscriptionsQuantity\"\n" +
                "from \"userSubscriptions\" as ust\n" +
                "inner join users u\n" +
                "on u.id = ust.\"subscribedToUser_id\"\n" +
                "where ust.user_id = ?;";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
        return users;
    }

    public Boolean subscribedToUser(Long subscribedToUserId, Long user_id){
        if (checkUser(subscribedToUserId)){
            LocalDate localDate = LocalDate.now();
            if (subscribedToUserId != user_id) {
                if (subscribedToUserId != null) {
                    Integer first = jdbcTemplate.update("INSERT INTO \"userSubscriptions\" (user_id, \"subscribedToUser_id\", date) VALUES (?,?,?)",
                            user_id, subscribedToUserId, localDate);
                    updateUserForAddSubscriptions(user_id);
                    updateUserForAddSubscribers(subscribedToUserId);
                    return true;
                }
            }
        }
        return false;
    }

    public void updateUserForAddSubscribers(Long user_id){
        String sql = "update users u \n" +
                "set \"subscribersQuantity\" = \"subscribersQuantity\" + 1" +
                "where u.id = ?";
        jdbcTemplate.update(sql, user_id);
    }

    public void updateUserForAddSubscriptions(Long user_id){
        String sql = "update users u \n" +
                "set \"subscriptionsQuantity\" = \"subscriptionsQuantity\" + 1" +
                "where u.id = ?";
        jdbcTemplate.update(sql, user_id);
    }

    public User getUserByEmail(HttpServletRequest httpServletRequest){

        String email = Utils.readCookie(httpServletRequest, userDao);
        try {
            return userDao.getUsersByEmail(email).get(0);
        }catch (Exception exc){
            return null;
        }

    }

    public Boolean checkUser(Long id){
        if (id != null) {
            String sql = "select id from users where id = ?";
            List<Long> users = jdbcTemplate.queryForList(sql, Long.class, id);
            if (users.size() != 0) {
                return true;
            }
        }
        return false;
    }
}
