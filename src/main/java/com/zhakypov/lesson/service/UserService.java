package com.zhakypov.lesson.service;

import com.zhakypov.lesson.dao.UserDao;
import com.zhakypov.lesson.dto.UserAddDataBaseDto;
import com.zhakypov.lesson.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<User> getUsers(){
        return userDao.getAllUsers();
    }

    public List<User> getUsersByName(String name){
        return userDao.getUsersByName(name);
    }

    public List<User> getUsersByLogin(String login){
        return userDao.getUsersByLogin(login);
    }

    public List<User> getUsersByEmail(String email){
        return userDao.getUsersByEmail(email);
    }

    public Boolean checkUserByEmail(String email){
        return userDao.checkUserByEmail(email);
    }

    public String register(UserAddDataBaseDto userAddDataBaseDto){
        return userDao.register(userAddDataBaseDto);
    }

    public String login(UserAddDataBaseDto userAddDataBaseDto, HttpHeaders response){
        return userDao.login(userAddDataBaseDto, response);
    }
}
