package com.inetum.training.user.domain;

public class User {

    public enum Role {
        USER, ADMIN
    }

    private String login;

    private String passwordHash;

    private Role role;

}
