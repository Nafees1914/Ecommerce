package com.Ecommerce.Ecommerce.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class CategoryCompositeKey implements Serializable
{
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_meta_data_field_id")
    private Long categoryMetaDataFieldId;
}
