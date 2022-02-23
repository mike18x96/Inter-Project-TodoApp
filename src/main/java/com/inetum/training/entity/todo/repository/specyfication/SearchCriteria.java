package com.inetum.training.entity.todo.repository.specyfication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}