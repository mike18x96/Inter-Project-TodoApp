package com.inetum.training.todo.persistance;

import com.inetum.training.todo.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TodoJpaRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE " +
            "(:name is null or t.name = :name) and " +
            "(:priority is null or t.priority = :priority)")
    Page<Todo> findBySearchParams(
            @Param("name") String name,
            @Param("priority") String priority,
            Pageable pageable);


}
