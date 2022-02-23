package com.inetum.training.entity.todo.repository.specyfication;

import com.inetum.training.entity.todo.model.Todo;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public TodoSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public TodoSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Todo> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(TodoSpecifications::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}