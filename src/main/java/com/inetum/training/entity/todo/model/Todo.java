package com.inetum.training.entity.todo.model;

import com.inetum.training.entity.user.model.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Entity
@Table(name = "Todo")
public class Todo implements Serializable {

    private static final long serialVersionUID = 376436963768417453L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private String priority;
    private String description;
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
