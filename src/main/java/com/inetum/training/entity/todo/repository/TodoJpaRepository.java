package com.inetum.training.entity.todo.repository;

import com.inetum.training.entity.todo.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TodoJpaRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {

    Page<Todo> findAllByUserId(Long id, Pageable pageable);

}
