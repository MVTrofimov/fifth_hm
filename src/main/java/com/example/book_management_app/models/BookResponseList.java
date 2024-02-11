package com.example.book_management_app.models;

import com.example.book_management_app.entities.Book;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookResponseList {

    private List<BookResponse> bookList = new ArrayList<>();

}
