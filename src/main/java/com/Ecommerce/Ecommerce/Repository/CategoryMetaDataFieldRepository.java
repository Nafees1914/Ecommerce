package com.Ecommerce.Ecommerce.Repository;


import com.Ecommerce.Ecommerce.Entity.CategoryMetaDataField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetaDataFieldRepository extends
        JpaRepository<CategoryMetaDataField, Long> {

  @Query(value = "SELECT * FROM category_meta_data_field a WHERE a.name = ?1", nativeQuery = true)
  CategoryMetaDataField findByCategoryMetadataFieldName(String fieldName);
  CategoryMetaDataField findByNameIgnoreCase(String key);
}