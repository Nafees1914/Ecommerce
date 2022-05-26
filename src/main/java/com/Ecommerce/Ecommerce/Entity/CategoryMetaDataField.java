package com.Ecommerce.Ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class CategoryMetaDataField
{
    @Id
    @SequenceGenerator(name="category_meta_data_field_sequence",sequenceName = "category_meta_data_field_sequence", initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_meta_data_field_sequence")
    private Long id;

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoryMetaDataField")
    private Set<CategoryMetaDataFieldValue> categoryMetaDataFieldValues = new HashSet<>();
}
