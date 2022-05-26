package com.Ecommerce.Ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Category
{

    @Id
    @SequenceGenerator(name="category_sequence",sequenceName = "category_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_sequence")
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category_id")
    @JsonBackReference
    private Category parentCategory;

    @JsonManagedReference
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<CategoryMetaDataFieldValue> categoryMetaDataFieldValues = new HashSet<>();



}
