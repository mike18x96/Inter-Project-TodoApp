package com.inetum.training.user.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 6760276400659841526L;

    public enum Role {
        USER, ADMIN
    }

    @NotNull
    @NotBlank
    @Id
    private String login;

    @Length(min = 5)
    private String passwordHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
