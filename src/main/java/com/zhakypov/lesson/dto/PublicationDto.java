package com.zhakypov.lesson.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicationDto {
    private Long id;
    private Long user_id;
    private String img;
    private String description;
    private LocalDate date;
}
