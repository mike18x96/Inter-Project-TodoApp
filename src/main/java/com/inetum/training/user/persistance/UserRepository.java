package com.inetum.training.user.persistance;

import com.inetum.training.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
