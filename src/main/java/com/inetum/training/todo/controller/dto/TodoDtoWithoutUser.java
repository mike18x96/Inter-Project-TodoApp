package com.inetum.training.todo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoDtoWithoutUser {

    private Long id;
    private String name;
    private String priority;
    private String description;
    private boolean completed;
    private Long userId;


}
