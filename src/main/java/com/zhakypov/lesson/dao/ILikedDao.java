package com.zhakypov.lesson.dao;

import com.zhakypov.lesson.model.Publication;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.utility.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ILikedDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public String getILiked(Long id, Long publication_id) {
        String sql = "select count(*) from \"iLikeds\" where user_id = ? and publication_id = ?";
        var number = jdbcTemplate.queryForObject(sql, Integer.class, id, publication_id);
        if (number == 0) {
            return "not liked";
        }
        return "liked";
    }

    public String setILiked(Long publication_id, Long user_id){
        if (check(publication_id)){
            if (user_id != -1){
                LocalDate localDate = LocalDate.now();
                String query = "INSERT INTO \"iLikeds\" (user_id, publication_id, date" +
                        " VALUES (?,?,?)";
                jdbcTemplate.update(query, new BeanPropertyRowMapper<>(Publication.class),
                        user_id, publication_id, localDate);
                return "Успешно";
            }
        }
        return "Ошибка";
    }

    public Boolean check (Long publication_id) {
        if (publication_id != null){
            String sql = "select id from publications where id = ?";
            List<Long> publications = jdbcTemplate.queryForList(sql, Long.class, publication_id);
            if (publications.size() != 0){
                return true;
            }
        }
        return false;
    }

    public Long getUserId (HttpServletRequest httpServletRequest) {

        String email = Utils.readCookie(httpServletRequest, userDao);
        List<User> users = userDao.getUsersByEmail(email);
        if (users.size() != 0){
            return users.get(0).getId();
        }
        return -1L;
    }
}
