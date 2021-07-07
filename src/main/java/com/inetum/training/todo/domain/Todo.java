package com.inetum.training.todo.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Todo implements Serializable{

    private static final long serialVersionUID = 376436963768417453L;

    private Long id;
    private String name;
    private String priority;
    private String description;
    private boolean completed;

}
