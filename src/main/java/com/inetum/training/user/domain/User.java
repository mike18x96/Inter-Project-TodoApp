package com.inetum.training.user.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class User implements Serializable {

    public enum Role {
        USER, ADMIN
    }

    @NotNull
    @Id
    private String login;

    @Min(5)
    @Getter(AccessLevel.NONE)
    private String passwordHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
