package com.zhakypov.lesson.service;

import com.zhakypov.lesson.dao.PublicationDao;
import com.zhakypov.lesson.dto.PublicationDto;
import com.zhakypov.lesson.model.Publication;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicationService {
    private final PublicationDao publicationDao;

    public List<PublicationDto> getAllPublications(){
        return publicationDao.getAllPublications();
    }

    public List<Publication> getPublicationAnotherUsers(Long user_id){
        return publicationDao.getPublicationAnotherUsers(user_id);
    }

    public List<Publication> getPublicationsSubscribedUsers(Long user_id){
        return publicationDao.getPublicationSubscribedUsers(user_id);
    }

    public String deletePublications(Long post_id,Long user_id){
        return publicationDao.deletePublication(user_id,post_id);
    }

    public String addPublications(PublicationDto publicationDto, Long user_id, MultipartFile file){
        String imgPath = saveImg(file);
        return publicationDao.addPublication(publicationDto,user_id,imgPath);
    }

    public PublicationDto createPublication(Long user_id,String description, MultipartFile file){
        String imgPath = saveImg(file);
        return publicationDao.createPublication(user_id,imgPath,description);
    }

    private String saveImg(MultipartFile file){
        String imgPath = file.getOriginalFilename();
        File convertFile = new File(
                "src\\main\\resources\\static\\"
                        + imgPath);
        try {
            convertFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream fout = new FileOutputStream(convertFile)){
            fout.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgPath;
    }
}
