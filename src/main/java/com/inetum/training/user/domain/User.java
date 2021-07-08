package com.inetum.training.user.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="login")
@ToString
@Builder
@Entity
@Table(name = "USERS")
public class User implements Serializable{

    private static final long serialVersionUID = 6760276400659841526L;

    public enum Role {
        USER, ADMIN
    }

    @Id
    @NotNull
    private String login;

    @NotNull
    @Length(min = 5)
    private String passwordHash;

    @NotNull
    private Role role;

}
