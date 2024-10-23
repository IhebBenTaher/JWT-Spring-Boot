package com.security.jwt.book;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;
    @PostMapping
    public ResponseEntity<?>save(@RequestBody BookRequest bookRequest){
        bookService.save(bookRequest);
        return ResponseEntity.accepted().build();
    }
    @GetMapping
    public ResponseEntity<List<Book>>findAll(){
        return ResponseEntity.ok(bookService.findAll());
    }
}
