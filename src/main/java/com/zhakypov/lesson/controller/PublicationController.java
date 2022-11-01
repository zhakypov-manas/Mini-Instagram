package com.zhakypov.lesson.controller;

import com.zhakypov.lesson.dao.PublicationDao;
import com.zhakypov.lesson.dto.PublicationDto;
import com.zhakypov.lesson.model.Publication;
import com.zhakypov.lesson.model.User;
import com.zhakypov.lesson.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class PublicationController {
    private final PublicationService publicationService;

    @GetMapping("/publications/")
    public ResponseEntity<?> getAllPublications(){
        return new ResponseEntity<>(publicationService.getAllPublications(),HttpStatus.OK);
    }

    @GetMapping("/publications/{id}")
    public ResponseEntity<?> getPublicationsAnotherUsers(@PathVariable Long id){
        return new ResponseEntity<List<Publication>>(publicationService.getPublicationAnotherUsers(id), HttpStatus.OK);
    }

    @GetMapping("/publicationsSubs/{id}")
    public ResponseEntity<?> getPublicationsSubscribedUsers(@PathVariable Long id){
        return new ResponseEntity<List<Publication>>(publicationService.getPublicationsSubscribedUsers(id), HttpStatus.OK);
    }

    @PostMapping("/publications/add/")
    public ResponseEntity<?> addPublications(@RequestPart PublicationDto publicationDto, Authentication authentication, @RequestPart("file")MultipartFile file){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<String>(publicationService.addPublications(publicationDto, user.getId(), file), HttpStatus.OK);
    }

    @PostMapping("/publications/delete/{id}")
    public ResponseEntity<?> deletePublications(@PathVariable Long id, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<String>(publicationService.deletePublications(id, user.getId()), HttpStatus.OK);
    }
}
