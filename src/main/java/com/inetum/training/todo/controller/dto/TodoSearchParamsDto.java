package com.inetum.training.todo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@Getter
public class TodoSearchParamsDto {

    private String name;
    private String priority;
    private Pageable pageRequest;
}
