package com.zhakypov.lesson.dao;

import com.zhakypov.lesson.dto.CommentDto;
import com.zhakypov.lesson.dto.PublicationDto;
import com.zhakypov.lesson.dto.UserAddDataBaseDto;
import com.zhakypov.lesson.model.Comment;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.utility.Utils;
import lombok.AllArgsConstructor;
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
public class CommentDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public List<CommentDto> getAllCommentInPublication(Long publication_id){
        String sql = "select * from \"comments\" where publication_id = ?";
        List<CommentDto> commentDtos = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CommentDto.class), publication_id);

        return commentDtos;
    }

    public List<Comment> getUserComments(Long id){
        String sql = "select * from \"comments\" where user_id = ?";
        List<Comment> comments = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), id);
        return comments;
    }

    public CommentDto createComment(CommentDto commentDto){
        LocalDateTime localDate = LocalDateTime.now();
        try {
            String query = "INSERT INTO comments (user_id, publication_id, \"commentText\", date)" +
                    " VALUES (?,?,?,?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(query, new String[]{"id"});
                ps.setLong(1,commentDto.getUser_id());
                ps.setLong(2, commentDto.getPublication_id());
                ps.setString(3,commentDto.getCommentText());
                ps.setTimestamp(4, Timestamp.valueOf(localDate));
                return ps;
            }, keyHolder);
            return CommentDto.builder()
                    .id(Objects.requireNonNull(keyHolder.getKey()).longValue())
                    .user_id(commentDto.getUser_id())
                    .publication_id(commentDto.getPublication_id())
                    .commentText(commentDto.getCommentText())
                    .date(localDate.toLocalDate()).build();
        }catch (Exception exc){
            return null;
        }

    }

    public String addComment(CommentDto commentDto, Long publicationId, String email){

        if (!email.equals(" ")) {
            String sql = "select user_id from \"publications\" where id = ?";
            Long user_id = jdbcTemplate.queryForObject(sql,Long.class,publicationId);
            if (user_id != null){
                if (checkLikedPost(user_id,publicationId)) {
                    if (commentDto.getCommentText() != null) {
                        LocalDate localDate = LocalDate.now();
                        String query = "INSERT INTO comments (user_id, publication_id, \"commentText\", date) VALUES (?,?,?,?)";
                        jdbcTemplate.update(query, email, publicationId, commentDto.getCommentText(), localDate);
                        return "Успешно";
                    }
                }
            }
        }
        return "Не получилось";
    }

    public Boolean deleteCommentInMyPost(Long comment_id, Long publication_id, String email){

        if (!email.equals(" ")) {
            if (checkCommentId(comment_id)) {
                return getUserPublications(email, publication_id, comment_id,userDao.getUsersByEmail(email).get(0).getId());
            }
        }
        return false;
    }

    public Boolean getUserPublications(String email, Long publication_id, Long comment_id, Long user_id){
        String sql = "select id from \"users\" where email = ?";
        List<Long> commentId = jdbcTemplate.queryForList(sql,Long.class, email);
        if (commentId.size() != 0){
            String query = "select id from \"publications\" where user_id = ?";
            List<Long> publicationId = jdbcTemplate.queryForList(query,Long.class, user_id);
            if (publicationId.size() != 0){
                deleteComment(comment_id, publication_id);
                return true;
            }
        }
        return false;
    }

    public void deleteComment(Long comment_id, Long publication_id){
        String sql = "DELETE FROM comments WHERE id = ? and publication_id = ?";
        jdbcTemplate.update(sql, comment_id, publication_id);
    }

    public Boolean checkCommentId(Long id){
        String sql = "select id from comments";
        List<Long> commentId = jdbcTemplate.queryForList(sql,Long.class);
        for (Long i : commentId){
            if(i == id){
                return true;
            }
        }
        return false;
    }

    public Boolean checkLikedPost(Long user_id, Long publication_id){
        String sql = "select count(*) from \"iLikeds\" where user_id = ? and publication_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, user_id, publication_id);
        if (count == 0){
            return false;
        }
        return true;
    }

}
