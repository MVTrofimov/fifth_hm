package com.example.book_management_app.service;

import com.example.book_management_app.entities.Book;
import com.example.book_management_app.entities.Category;
import com.example.book_management_app.exceptions.EntityNotFoundException;
import com.example.book_management_app.repositories.BookRepository;
import com.example.book_management_app.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final CacheManager cacheManager;
    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Book findById(Long id){
        return bookRepository.findById(id).orElseThrow();
    }

    @Cacheable(value = "bookByNameAndAuthor", key = "#name + #author")
    public Book findByNameAndAuthor(String name, String author) {
        Book probe = new Book();
        probe.setName(name);
        probe.setAuthor(author);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "cost", "category");
        Example<Book> example = Example.of(probe, matcher);

        return bookRepository.findOne(example).orElseThrow();
    }

    @Cacheable(value = "bookByCategoryName", key = "#name")
    public List<Book> findAllByCategoryName(String name){
        Long idOfCategory = categoryRepository.findCategoryByName(name).getId();
        return bookRepository.findByCategoryId(idOfCategory);
    }

    @Caching(evict = {
            @CacheEvict(value = "bookByNameAndAuthor", allEntries = true),
            @CacheEvict(value = "bookByCategoryName", allEntries = true)}

    )
    public Book save(Book book){
        return bookRepository.save(book);
    }

    @Caching(put = {
            @CachePut(value = "bookByNameAndAuthor", key = "#book.name + #book.author"),
            @CachePut(value = "bookByCategoryName", key = "#book.category.name")
    })
    public Book update(Book book){
        Category category = book.getCategory();
        Book existedBook = findById(book.getId());
        BeanUtils.copyProperties(book, existedBook);

        existedBook.setCategory(category);
        return save(existedBook);
    }


    public void deleteById(Long id){
        Book book = findById(id);
        StringBuilder bookByNameAndAuthor_key = new StringBuilder();
        bookByNameAndAuthor_key.append(book.getName());
        bookByNameAndAuthor_key.append(book.getAuthor());
        Category category = categoryRepository.findById(book.getCategory().getId()).orElseThrow(()
                -> new EntityNotFoundException(MessageFormat.format("Категории с ID: {0} не найдено", id)));
        cacheManager.getCache("bookByNameAndAuthor").evict(bookByNameAndAuthor_key);
        cacheManager.getCache("bookByCategoryName").evict(category.getName());
        bookRepository.deleteById(id);
    }

}
