package com.Ecommerce.Ecommerce.Dto;

import com.Ecommerce.Ecommerce.Entity.Product;
import lombok.Data;

@Data
public class UpdateProductVariationDto
{

  private Product product;
  private Integer quantityAvailable;
  private Double price;
  private String metadata;
  private Boolean isActive;





}
