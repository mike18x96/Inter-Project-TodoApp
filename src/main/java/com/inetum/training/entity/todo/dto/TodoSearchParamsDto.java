package com.inetum.training.entity.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoSearchParamsDto {

    private String name;
    private String priority;

}
