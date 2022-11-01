package com.zhakypov.lesson.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Comment {
    private Long id;
    private Long user_id;
    private Long publication_id;
    private String commentText;
    private LocalDate date;
}
