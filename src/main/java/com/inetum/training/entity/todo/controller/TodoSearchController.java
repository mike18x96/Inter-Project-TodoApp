package com.inetum.training.entity.todo.controller;

import com.inetum.training.entity.todo.model.Todo;
import com.inetum.training.entity.todo.repository.specyfication.TodoSpecificationsBuilder;
import com.inetum.training.entity.todo.service.TodoSearchService;
import com.inetum.training.entity.user.model.User;
import com.inetum.training.security.LoggedCurrentUser;
import com.inetum.training.entity.todo.dto.TodoDtoWithoutUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/search/todos")
@RequiredArgsConstructor
public class TodoSearchController {

    private final TodoSearchService searchService;
    private final LoggedCurrentUser loggedCurrentUser;

    @GetMapping
    public Page<TodoDtoWithoutUser> getAll(@RequestParam(value = "search", required = false) String search, Pageable pageable) {
        TodoSpecificationsBuilder builder = new TodoSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(=|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Todo> spec;
        if (loggedCurrentUser.getCurrentUser().getRole().equals("ROLE_" + User.Role.ADMIN.name())) {
            spec = builder.build();
        } else {
            spec = builder
                    .with("user", "=", loggedCurrentUser.getCurrentUser().getId())
                    .build();
        }
        return searchService.searchByParams(spec, pageable);
    }


    //    @RequestMapping(method = RequestMethod.GET)
//    public Page<Todo> getAll(@RequestParam(value="name", required = false) String name,
//                             @RequestParam(value="priority", required = false) String priority,
//                             Pageable pageable){
//        return searchService.find(new TodoSearchParamsDto(name, priority), pageable);
//    }
}
