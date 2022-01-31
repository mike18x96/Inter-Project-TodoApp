package com.inetum.training.todo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoSearchParamsDto {

    private String name;
    private String priority;

}
