package com.tequila.ecommerce.vinoteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tequila.ecommerce.vinoteca.models.Category;
import com.tequila.ecommerce.vinoteca.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByCategoryId(Long categoryId);
}
