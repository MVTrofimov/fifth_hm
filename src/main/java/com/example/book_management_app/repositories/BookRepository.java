package com.example.book_management_app.repositories;

import com.example.book_management_app.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM BOOK WHERE category_id = :categoryId", nativeQuery = true)
    List<Book> findByCategoryId(@Param("categoryId") Long id);
}
