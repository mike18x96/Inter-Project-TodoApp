package com.inetum.training.user.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Builder
@Table(name="USERS")
public class User implements Serializable {

    private static final long serialVersionUID = 6760276400659841526L;

    public enum Role {
        USER, ADMIN
    }

    @NotNull
    @NotBlank
    @Id
    @Column(name = "login")
    private String login;

    private String passwordHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
