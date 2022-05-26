package com.Ecommerce.Ecommerce.Dto;

import lombok.Data;

@Data
public class ProductVariationDto {

    private Integer productId;
    private String metaData;
    private Integer quantity;
    private Float price;


}
