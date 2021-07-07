package com.inetum.training.todo.persistance;

import com.inetum.training.todo.domain.Todo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TodoJpaRepository extends CrudRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE " +
            "(:name is null or t.name = :name) and " +
            "(:priority is null or t.priority = :priority)")
    Iterable<Todo> findBySearchParams(
            @Param("name") String name,
            @Param("priority") String priority);


}
