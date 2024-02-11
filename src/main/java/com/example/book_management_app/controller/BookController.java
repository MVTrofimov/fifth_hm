package com.example.book_management_app.controller;

import com.example.book_management_app.entities.Book;
import com.example.book_management_app.mapper.BookMapper;
import com.example.book_management_app.models.BookResponse;
import com.example.book_management_app.models.BookResponseList;
import com.example.book_management_app.models.UpsertEntityRequest;
import com.example.book_management_app.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookMapper mapper;

    @GetMapping
    public ResponseEntity<BookResponseList> findAll(){

        return ResponseEntity.ok(mapper.bookListToBookResponseList(bookService.findAll()));
    }

    @GetMapping("/name_author")
    public ResponseEntity<BookResponse> findByNameAndAuthor (@RequestParam String name, @RequestParam String author){

        return ResponseEntity.ok(mapper.bookToBookResponse(bookService.findByNameAndAuthor(name, author)));
    }


    @GetMapping("/category_name")
    public ResponseEntity<BookResponseList> findAllByCategory(@RequestParam String categoryName){

        return ResponseEntity.ok(mapper.bookListToBookResponseList(bookService.findAllByCategoryName(categoryName)));
    }


    @PostMapping
    public ResponseEntity<BookResponse> create (@RequestBody UpsertEntityRequest request){
        Book newBook = bookService.save(mapper.requestToBook(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.bookToBookResponse(newBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update (@PathVariable Long id, @RequestBody UpsertEntityRequest request){
        Book updatedBook = bookService.update(mapper.requestToBook(id, request));

        return ResponseEntity.ok(mapper.bookToBookResponse(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        bookService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
