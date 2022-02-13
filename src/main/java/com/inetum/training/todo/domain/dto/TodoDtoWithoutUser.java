package com.inetum.training.todo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class TodoDtoWithoutUser {

    private Long id;
    private String name;
    private String priority;
    private String description;
    private boolean completed;
    private Long userId;


}
