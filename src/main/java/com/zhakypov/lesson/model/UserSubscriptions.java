package com.zhakypov.lesson.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserSubscriptions {
    private Long id;
    private Long user_id;
    private Long subscribedToUser_id;
    private LocalDate date;
}
