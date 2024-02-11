package com.example.book_management_app.models;

import lombok.Data;

@Data
public class BookResponse {
    private Long bookId;
    private String bookName;

    private String author;

    private Integer cost;
}
