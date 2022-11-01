package com.zhakypov.lesson.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Publication {
    private Long id;
    private Long user_id;
    private String img;
    private String description;
    private LocalDate date;
}
