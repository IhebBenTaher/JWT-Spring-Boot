package com.security.jwt.book;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookRequest {
    private Integer Id;
    private String author;
    private String isbn;
}
