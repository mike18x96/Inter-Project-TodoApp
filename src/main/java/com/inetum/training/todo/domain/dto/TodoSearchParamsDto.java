package com.inetum.training.todo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TodoSearchParamsDto {

    private String name;
    private String priority;

}
