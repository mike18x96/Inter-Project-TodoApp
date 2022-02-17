package com.inetum.training.book.controller;


import com.inetum.training.book.domain.Book;
import com.inetum.training.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Book> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Book book) {
        book.setId(null);
        bookService.save(book);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Book> get(@PathVariable("id") Long id) {
        try {
            return bookService.findById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Book book, @PathVariable("id") Long id) {
        try {
            bookService.update(book, id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        try {
            bookService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String notFoundHandler(EntityNotFoundException e) {
        return String.format("Not found element with id: %s", e.getMessage());
    }



}
