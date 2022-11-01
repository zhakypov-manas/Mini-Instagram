package com.zhakypov.lesson.controller;

import com.zhakypov.lesson.dao.*;
import com.zhakypov.lesson.dto.UserAddDataBaseDto;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<List<User>>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/findByName/{name}")
    public ResponseEntity<?> getUsersByName(@PathVariable String name){
        return new ResponseEntity<List<User>>(userService.getUsersByName(name), HttpStatus.OK);
    }

    @GetMapping("/users/findByLogin/{login}")
    public ResponseEntity<?> getUsersByLogin(@PathVariable String login){
        return new ResponseEntity<List<User>>(userService.getUsersByLogin(login), HttpStatus.OK);
    }

    @GetMapping("/users/findByEmail/{email}")
    public ResponseEntity<?> getUsersByEmail(@PathVariable String email){
        return new ResponseEntity<List<User>>(userService.getUsersByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/users/checkByEmail/{email}")
    public ResponseEntity<?> checkUserByEmail(@PathVariable String email){
        return new ResponseEntity<Boolean>(userService.checkUserByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/users/register/")
    public ResponseEntity<?> register(@RequestBody UserAddDataBaseDto userAddDataBaseDto){
        return new ResponseEntity<String>(userService.register(userAddDataBaseDto), HttpStatus.OK);
    }

    @PostMapping("/users/login/")
    public ResponseEntity<?> login(@RequestBody UserAddDataBaseDto userAddDataBaseDto){
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<String>(userService.login(userAddDataBaseDto, httpHeaders), httpHeaders, HttpStatus.OK);
    }
}
