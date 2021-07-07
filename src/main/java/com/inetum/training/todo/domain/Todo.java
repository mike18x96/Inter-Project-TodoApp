package com.inetum.training.todo.domain;

import java.io.Serializable;

public class Todo implements Serializable{

    private static final long serialVersionUID = 376436963768417453L;

    private Long id;
    private String name;
    private String priority;
    private String description;
    private boolean completed;

    public Todo(){ }

    public Todo(Long id, String name, String priority, String description, boolean completed) {

        this.id = id;
        this.name = name;
        this.priority = priority;
        this.description = description;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
