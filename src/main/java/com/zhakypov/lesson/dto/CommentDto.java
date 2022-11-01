package com.zhakypov.lesson.dto;

import com.zhakypov.lesson.model.User;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CommentDto {
    private Long id;
    private Long user_id;
    private Long publication_id;
    private String commentText;
    private LocalDate date;
}
