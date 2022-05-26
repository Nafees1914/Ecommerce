package com.Ecommerce.Ecommerce.Dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddProductDto {

  @NotNull(message = "Brand cannot be NULL")
  private  String brand;

  @NotNull(message = "Category Id should be there to map")
  private Integer categoryId;
  @NotNull(message = "Add a description")
  private String description;
  @NotNull(message = "Name should not be Blank")
  private  String name;


}
