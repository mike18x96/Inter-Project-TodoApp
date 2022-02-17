package com.inetum.training.book.service;

import com.inetum.training.book.domain.Book;
import com.inetum.training.book.persistance.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    @Cacheable(cacheNames = "AllBooks")
    public Page<Book> findAll(Pageable pageable) {
        simulateSlowService();
        return bookRepository.findAll(pageable);
    }

    @Cacheable(cacheNames = "OneBook", key = "#id")
    public Optional<Book> findById(Long id) {
        simulateSlowService();
        if (existsById(id)) {
            return bookRepository.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Long save(Book book) {
        return bookRepository.save(book).getId();
    }

    @CachePut(cacheNames = "OneBook", key = "#result.id")
    public Book update(Book book, Long id) {
        if (existsById(id)) {
            book.setId(id);
            save(book);
            return book;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @CacheEvict(cacheNames = "OneBook")
    public void delete(Long id) {
        if (existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
