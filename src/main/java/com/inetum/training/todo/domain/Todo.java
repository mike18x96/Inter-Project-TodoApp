package com.inetum.training.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "TODOS")
public class Todo implements Serializable{

    private static final long serialVersionUID = 376436963768417453L;

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String priority;
    private String description;
    private boolean completed;

}
