package com.zhakypov.lesson.controller;

import com.zhakypov.lesson.dto.PublicationDto;
import com.zhakypov.lesson.model.Publication;
import com.zhakypov.lesson.service.PublicationService;
import com.zhakypov.lesson.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final PublicationService publicationService;
    private final UserService userService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<?> addPost(@RequestPart String user_id,@RequestPart  String description,@RequestPart MultipartFile file){
        PublicationDto publication = publicationService.createPublication(Long.parseLong(user_id), description, file);
        return new ResponseEntity<PublicationDto>(publication, HttpStatus.OK);
    }

    @GetMapping(
            value = "/",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImageDynamicType(@RequestParam("jpg") boolean jpg, @PathVariable String name) {
        MediaType contentType = jpg ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
        InputStream in = getClass().getResourceAsStream("/" + name);
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }
}
