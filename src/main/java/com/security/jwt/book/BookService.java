package com.security.jwt.book;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    public void save(BookRequest bookRequest) {
        Book book=Book.builder().id(bookRequest.getId()).author(bookRequest.getAuthor()).isbn(bookRequest.getIsbn()).build();
        bookRepository.save(book);
    }
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

}
