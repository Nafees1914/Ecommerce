package com.Ecommerce.Ecommerce.Repository;

import com.Ecommerce.Ecommerce.Entity.CategoryMetaDataFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryMetaDataFieldValuesRepository extends
        JpaRepository<CategoryMetaDataFieldValue, Long> {

  @Query(value = "SELECT * FROM category_meta_data_field_value a WHERE a.category_meta_data_field_id=?1", nativeQuery = true)
  Optional<CategoryMetaDataFieldValue> findByCategoryMetadataFieldId(Long id);


  List<CategoryMetaDataFieldValue> findByCategoryId(Long id);
}

