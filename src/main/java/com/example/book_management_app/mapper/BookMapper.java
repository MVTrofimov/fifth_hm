package com.example.book_management_app.mapper;

import com.example.book_management_app.entities.Book;
import com.example.book_management_app.entities.Category;
import com.example.book_management_app.models.BookResponse;
import com.example.book_management_app.models.BookResponseList;
import com.example.book_management_app.models.UpsertEntityRequest;
import com.example.book_management_app.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final CategoryRepository repository;

    public BookResponse bookToBookResponse(Book book){
        BookResponse bookResponse = new BookResponse();
        bookResponse.setBookId(book.getId());
        bookResponse.setBookName(book.getName());
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setCost(book.getCost());

        return bookResponse;
    }

    public Book requestToBook(UpsertEntityRequest request){
        Book book = new Book();
        book.setName(request.getBookName());
        book.setAuthor(request.getAuthor());
        book.setCost(request.getCost());
        Category category = repository.findCategoryByName(request.getCategoryName());
        if (category == null){
            category = new Category();
            category.setName(request.getCategoryName());
            repository.save(category);
            book.setCategory(category);
        } else {
            book.setCategory(repository.findCategoryByName(request.getCategoryName()));
        }
        return book;
    }

    public Book requestToBook(Long id, UpsertEntityRequest request){
        Book book = requestToBook(request);
        book.setId(id);

        return book;
    }

    public BookResponseList bookListToBookResponseList(List<Book> bookList){
        BookResponseList bookResponseList = new BookResponseList();
        for (Book b : bookList){
            bookResponseList.getBookList().add(bookToBookResponse(b));
        }
        return bookResponseList;
    }



}
