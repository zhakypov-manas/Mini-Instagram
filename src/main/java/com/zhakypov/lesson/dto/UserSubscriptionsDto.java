package com.zhakypov.lesson.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSubscriptionsDto {
    private Long id;
    private Long user_id;
    private Long subscribedToUser_id;
    private LocalDate date;
}
