package com.Ecommerce.Ecommerce.Dto.Request;

import lombok.Data;

import java.util.Set;

@Data
public class AddCategoryMetaDataFieldValueDTO {

  private Set<String> metaDataFieldValues;
}