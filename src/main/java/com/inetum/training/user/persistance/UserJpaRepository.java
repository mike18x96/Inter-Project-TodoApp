package com.inetum.training.user.persistance;

import com.inetum.training.user.controller.dto.UserDtoWithoutPassword;
import com.inetum.training.user.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends CrudRepository<User, String> {

    @Query(value = "SELECT User.login, User .role FROM User")
    List<UserDtoWithoutPassword> getUserWithoutPassword();
}
