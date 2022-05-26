package com.Ecommerce.Ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CategoryMetaDataFieldValue
{
    @EmbeddedId
    private CategoryCompositeKey id = new CategoryCompositeKey();

    private String value;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @MapsId("categoryMetaDataFieldId")
    @JoinColumn(name = "category_meta_data_field_id")
    @JsonBackReference
    private CategoryMetaDataField categoryMetaDataField;
}
