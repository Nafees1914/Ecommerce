package com.Ecommerce.Ecommerce.Repository;

import com.Ecommerce.Ecommerce.Entity.Category;
import com.Ecommerce.Ecommerce.Entity.Product;
import com.Ecommerce.Ecommerce.Entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {

  boolean findByName(String name);

  Optional<Product> findByNameAndSellerAndCategoryAndBrand(String productName, Seller seller, Category associatedCategory, String brandName);

  List<Product> findBySeller(Seller seller);

  List<Product> findByCategory(Category category);
}
