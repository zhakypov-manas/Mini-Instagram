package com.zhakypov.lesson.dao;

import com.zhakypov.lesson.dto.CommentDto;
import com.zhakypov.lesson.dto.PublicationDto;
import com.zhakypov.lesson.dto.UserAddDataBaseDto;
import com.zhakypov.lesson.dto.UserSubscriptionsDto;
import com.zhakypov.lesson.model.Publication;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.utility.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PublicationDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final UserSubscriptionsDao userSubscriptionsDao;

    public List<PublicationDto> getAllPublications(){
        String sql = "select * from \"publications\"";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PublicationDto.class));
    }

    public List<Publication> getPublicationAnotherUsers(Long user_id) {

        List<User> users = userSubscriptionsDao.getSubscriptions(user_id);
        List<Publication> allPublications = new ArrayList<>();
        for(User s : users){
            String sql = "select * from \"publications\" where user_id != ?";
            List<Publication> publications = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Publication.class), s.getId());
            for(int i = 0; i < publications.size(); i++){
                allPublications.add(publications.get(i));
            }
        }

        return allPublications;
    }

    public List<Publication> getPublicationSubscribedUsers(Long user_id){
        String sql = "select \"subscribedToUser_id\" from \"userSubscriptions\" where user_id = ?";
        List<Integer> users = jdbcTemplate.queryForList(sql, Integer.class, user_id);

        List<Publication> allPublications = new ArrayList<>();
        for (Integer ui : users) {
            String query = "select * from \"publications\" where user_id = ?";
            List<Publication> publications = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Publication.class), ui);
            for(int i = 0; i < publications.size(); i++){
                allPublications.add(publications.get(i));
            }
        }
        return allPublications;
    }

    public String deletePublication(Long user_id,Long postId){
        String query = "select id from \"publications\" where id != ? and user_id = ?";
        List<Long> publicationId = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Long.class), postId, user_id);
        if (publicationId.size() != 0) {
            for (Long post : publicationId) {
                if (post == postId) {
                    deleteCommentFromPublication(postId);
                    String sql = "DELETE FROM publications WHERE id = ?";
                    jdbcTemplate.update(sql, postId);
                    updateUserForRemovePublication(user_id);
                    return "Публикация удалена";
                }
            }
        }
        return "ошибка";
    }

    private Long getUserId(HttpServletRequest httpServletRequest) {

        String email = Utils.readCookie(httpServletRequest, userDao);
        try {
            return userDao.getUsersByEmail(email).get(0).getId();
        }catch (Exception exc){
            return -1L;
        }

    }


    public void deleteCommentFromPublication(Long id){
        String query = "DELETE FROM comments where publication_id = ?";
            jdbcTemplate.update(query, id);
    }

    public String addPublication(PublicationDto publicationDto, Long user_id, String img_name){
        LocalDate localDate = LocalDate.now();
        String query = "INSERT INTO publications (user_id, img, description, date)" +
                " VALUES (?,?,?,?)";

        String img = "static/" + img_name;
        jdbcTemplate.update(query,
                user_id, img, publicationDto.getDescription(), localDate);
        updateUserForAddPublication(user_id);
        return "Успешная публикация";
    }

    public PublicationDto createPublication(Long user_id, String img_name, String description){
        LocalDateTime localDate = LocalDateTime.now();
        try {
            String query = "INSERT INTO publications (user_id, img, description, date)" +
                    " VALUES (?,?,?,?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(query, new String[]{"id"});
                ps.setLong(1,user_id);
                ps.setString(2, img_name);
                ps.setString(3,description);
                ps.setTimestamp(4,Timestamp.valueOf(localDate));
                return ps;
            }, keyHolder);
            updateUserForAddPublication(user_id);
            return PublicationDto.builder()
                    .id(Objects.requireNonNull(keyHolder.getKey()).longValue())
                    .user_id(user_id)
                    .img(img_name)
                    .description(description)
                    .date(localDate.toLocalDate()).build();


        }catch (Exception exc){
            return null;
        }


    }

    public void updateUserForAddPublication(Long user_id){
        String sql = "update users u \n" +
                "set \"publicationsQuantity\" = \"publicationsQuantity\" + 1" +
                "where u.id = ?";
        jdbcTemplate.update(sql, user_id);
    }

    public void updateUserForRemovePublication(Long user_id){
        String sql = "update users u \n" +
                "set \"publicationsQuantity\" = \"publicationsQuantity\" + 1" +
                "where u.id = ?";
        jdbcTemplate.update(sql, user_id);
    }
}
