package com.example.book_management_app.models;

import lombok.Data;

@Data
public class UpsertEntityRequest {

    private String bookName;

    private String author;

    private Integer cost;

    private String categoryName;

}
