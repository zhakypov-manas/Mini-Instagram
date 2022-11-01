package com.zhakypov.lesson.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String login;
    private String email;
    private String password;
    private Integer publicationsQuantity;
    private Integer subscribersQuantity;
    private Integer subscriptionsQuantity;
    private Boolean enabled;
}
