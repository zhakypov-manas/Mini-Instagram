package com.zhakypov.lesson.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddDataBaseDto {
    private Long id;
    private String name;
    private String login;
    private String email;
    private String password;
    private Integer publicationsQuantity;
    private Integer subscribersQuantity;
    private Integer subscriptionsQuantity;
}
