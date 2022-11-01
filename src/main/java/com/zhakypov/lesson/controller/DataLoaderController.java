package com.zhakypov.lesson.controller;

import com.zhakypov.lesson.DataLoader;
import com.zhakypov.lesson.dto.UserAddDataBaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DataLoaderController {

    private final DataLoader dataLoader;

//    @GetMapping("/create")
//    public void addDatabase(){
//        dataLoader.start();
//    }
}
