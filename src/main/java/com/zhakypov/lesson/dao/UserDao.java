package com.zhakypov.lesson.dao;

import com.zhakypov.lesson.dto.UserAddDataBaseDto;
import com.zhakypov.lesson.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    public UserAddDataBaseDto loginUser = null;
    private final PasswordEncoder encoder;


    public List<User> getAllUsers(){
        String sql = "select * from \"users\"";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public String register(@RequestBody UserAddDataBaseDto user){
        if (checkUserByEmail(user.getEmail())){
            return "Такой пользователь уже существует";
        }else {
            if (checkRegister(user)){
                addUser(user);
                addAuthorities(user);
                return "Успешная регистрация";
            }else {
                return "Вы не заполнили какое то поле";
            }


        }
    }

    public void addAuthorities(UserAddDataBaseDto user){
        String sql = "INSERT INTO authorities(authority, username) VALUES(?,?)";
        jdbcTemplate.update(sql, "ROLE_USER", user.getEmail());
    }

    public Boolean checkRegister(UserAddDataBaseDto user){
        if (user.getName() == null){
            return false;
        }else if (user.getEmail() == null){
            return false;
        }else if (user.getPassword() == null){
            return false;
        }else if (user.getLogin() == null){
            return false;
        }
        return true;
    }

    public String login(UserAddDataBaseDto user, HttpHeaders response){
        String sql = "select * from \"users\" where email = ? and password = ?";
        try {
            UserAddDataBaseDto lUser = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserAddDataBaseDto.class), user.getEmail(), user.getPassword());
            if (lUser != null) {
                Cookie cookie = new Cookie("user-email", lUser.getEmail());
                loginUser = lUser;
                response.add("Set-Cookie", cookieString(cookie));
                return "Успешная авторизация";
            }
        }catch (Exception exc){
            return "Ошика";
        }
        return "Вы не смогли зайти в аккаунт";
    }

    public String cookieString(Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        Charset utf8 = StandardCharsets.UTF_8;

        String encName = URLEncoder.encode(cookie.getName().strip(), utf8);
        String stringValue = cookie.getValue();
        String encValue = URLEncoder.encode(stringValue.strip(), utf8);

        sb.append(String.format("%s=%s", encName, encValue));
        if (cookie.getSecure()) {
            sb.append("; HttpOnly");
        }
        return sb.toString();
    }

    public void addUser(UserAddDataBaseDto user){
        String query = "INSERT INTO users (name, login, email, password, \"publicationsQuantity\", \"subscribersQuantity\", \"subscriptionsQuantity\") VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(query,
                user.getName(),user.getLogin(),user.getEmail(),encoder.encode(user.getPassword()),0,0,0);
    }

    public List<User> getUsersByName(String name) {
        String sql = "select * from \"users\" where name = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), name);
        return users;
    }

    public List<User> getUsersByLogin(String login) {
        String sql = "select * from \"users\" where login = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), login);
        return users;
    }

    public List<User> getUsersByEmail(String email) {
        String sql = "select * from \"users\" where email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return users;
    }

    public Boolean checkUserByEmail(String email){
        String sql = "select * from \"users\" where email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        if (users.size() != 0){
            return true;
        }else {
            return false;
        }
    }

}
