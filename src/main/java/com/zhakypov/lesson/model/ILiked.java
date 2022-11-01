package com.zhakypov.lesson.model;


import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ILiked {
    private Long id;
    private Long user_id;
    private Long publication_id;
    private LocalDate date;
}
