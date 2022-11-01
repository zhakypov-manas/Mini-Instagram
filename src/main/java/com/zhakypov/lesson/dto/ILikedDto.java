package com.zhakypov.lesson.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ILikedDto {
    private Long id;
    private Long user_id;
    private Long publication_id;
    private LocalDate date;
}
