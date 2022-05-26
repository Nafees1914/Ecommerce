package com.Ecommerce.Ecommerce.Dto;

import com.Ecommerce.Ecommerce.Entity.Category;
import lombok.Data;

@Data
public class ProductViewDto {


  private Integer id;

  private String name;

  private String description;

  private String brand;

  private Boolean isActive;

  private Boolean isCancellable;

  private Boolean isReturnable;

  private Category category;

}