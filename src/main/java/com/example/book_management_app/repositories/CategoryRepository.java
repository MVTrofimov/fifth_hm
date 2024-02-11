package com.example.book_management_app.repositories;

import com.example.book_management_app.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM CATEGORY WHERE name = :name", nativeQuery = true)
    Category findCategoryByName(@Param("name") String name);
}
