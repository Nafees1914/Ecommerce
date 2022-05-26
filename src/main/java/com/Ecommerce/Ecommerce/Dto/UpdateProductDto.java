package com.Ecommerce.Ecommerce.Dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateProductDto {

  private String name;
  private String brand;
  private Boolean isCancellable;
  private Boolean isReturnable;
  private String description;


}
