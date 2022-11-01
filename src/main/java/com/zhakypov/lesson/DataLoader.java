package com.zhakypov.lesson;

import com.zhakypov.lesson.dao.UserDao;
import com.zhakypov.lesson.dto.*;
import com.zhakypov.lesson.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class DataLoader {
    private final JdbcTemplate jdbcTemplate;
    LocalDate date = LocalDate.now();
    private final PasswordEncoder encoder;
    private final UserDao userDao;

//    @Bean
//    public void start(){
//        createUserAndAdd();
//        createPublicationAndAdd();
//        createCommentAndAdd();
//        createILikedAndAdd();
//        createUserSubscriptionsAndAdd();
//    }
    private void adminCreate(){
        UserAddDataBaseDto user = new UserAddDataBaseDto();
        user.setName("admin");
        user.setLogin("admin");
        user.setEmail("admin@mail.ru");
        user.setPassword(encoder.encode("admin"));
        userDao.addUser(user);
    }

    private void createUserSubscriptionsAndAdd(){
        UserSubscriptionsDto userSubscriptionsDto = UserSubscriptionsDto.builder()
                .user_id(1L)
                .subscribedToUser_id(2L)
                .date(date)
                .build();

        UserSubscriptionsDto userSubscriptionsDto1 = UserSubscriptionsDto.builder()
                .user_id(2L)
                .subscribedToUser_id(1L)
                .date(date)
                .build();

        addNewUserSubscription(userSubscriptionsDto);
        addNewUserSubscription(userSubscriptionsDto1);
    }

    private void createILikedAndAdd(){
        ILikedDto iLikedDto = ILikedDto.builder()
                .user_id(1L)
                .publication_id(2L)
                .date(date)
                .build();
        ILikedDto iLikedDto1 = ILikedDto.builder()
                .user_id(2L)
                .publication_id(1L)
                .date(date)
                .build();

        addNewILiked(iLikedDto);
        addNewILiked(iLikedDto1);
    }

    private void createCommentAndAdd(){
        CommentDto commentDto = CommentDto.builder()
                .user_id(1L)
                .publication_id(1L)
                .commentText("Какой то коммент")
                .date(date)
                .build();
        CommentDto commentDto1 = CommentDto.builder()
                .user_id(2L)
                .publication_id(2L)
                .commentText("Опять же какой то коммент")
                .date(date)
                .build();
        addNewComment(commentDto);
        addNewComment(commentDto1);
    }

    private void createPublicationAndAdd(){
        PublicationDto publicationDto = PublicationDto.builder()
                .id(1L)
                .user_id(1L)
                .img("monkey.jpg")
                .description("Что то")
                .date(date)
                .build();
        PublicationDto publicationDto1 = PublicationDto.builder()
                .id(2L)
                .user_id(2L)
                .img("test.jpg")
                .description("Опять что то")
                .date(date)
                .build();
        addNewPublication(publicationDto);
        addNewPublication(publicationDto1);
    }
    private void createUserAndAdd(){
        var pasha = UserAddDataBaseDto.builder()
                .id(1L)
                .name("pasha")
                .login("pashaTop")
                .email("pasha@mail.ru")
                .password("asdfad")
                .publicationsQuantity(0)
                .subscribersQuantity(0)
                .subscriptionsQuantity(0)
                .build();
        var sasha = UserAddDataBaseDto.builder()
                .id(2L)
                .name("Саша")
                .login("sashaTop")
                .email("sasha@mail.ru")
                .password("dafdsa")
                .publicationsQuantity(0)
                .subscribersQuantity(0)
                .subscriptionsQuantity(0)
                .build();
        addNewUser(pasha);
        addNewUser(sasha);
    }

    private int addNewUser(UserAddDataBaseDto user){
        String query = "INSERT INTO users (name, login, email, password, \"publicationsQuantity\", \"subscribersQuantity\", \"subscriptionsQuantity\") VALUES (?,?,?,?,?,?,?)";

        return jdbcTemplate.update(query,
                user.getName(),user.getLogin(),user.getEmail(),user.getPassword(),user.getPublicationsQuantity(),user.getSubscribersQuantity(),user.getSubscriptionsQuantity());

    }

    private int incrementPublications(Long id){
        String sql = "update users u \n" +
                "set \"publicationsQuantity\" = \"publicationsQuantity\" + 1" +
                "where u.id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private int addNewPublication(PublicationDto dto){
        var first = jdbcTemplate.update("INSERT INTO publications (user_id, img, description, date) VALUES (?,?,?,?)",
                dto.getUser_id(), dto.getImg(), dto.getDescription(), dto.getDate());
        var sql = incrementPublications(dto.getUser_id());
        return first;
    }

    private int addNewComment(CommentDto dto){
        return jdbcTemplate.update("INSERT INTO comments (user_id, publication_id, \"commentText\", date) VALUES (?,?,?,?)",
                dto.getUser_id(), dto.getPublication_id(), dto.getCommentText(), dto.getDate());
    }

    private int addNewILiked(ILikedDto dto){
        return jdbcTemplate.update("INSERT INTO \"iLikeds\" (user_id, publication_id,date) VALUES (?,?,?)",
                dto.getUser_id(), dto.getPublication_id(), dto.getDate());
    }

    private int addNewUserSubscription(UserSubscriptionsDto dto){
        var first = jdbcTemplate.update("INSERT INTO \"userSubscriptions\" (user_id, \"subscribedToUser_id\", date) VALUES (?,?,?)",
                dto.getUser_id(), dto.getSubscribedToUser_id(), dto.getDate());
        var second = incrementSubscriptions(dto.getUser_id());
        var third = incrementSubscribers(dto.getSubscribedToUser_id());
        return first;
    }

    private int incrementSubscribers(Long id){
        String sql = "update users u \n" +
                "set \"subscribersQuantity\" = \"subscribersQuantity\" + 1" +
                "where u.id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private int incrementSubscriptions(Long id){
        String sql = "update users u \n" +
                "set \"subscriptionsQuantity\" = \"subscriptionsQuantity\" + 1" +
                "where u.id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
