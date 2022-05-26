package com.Ecommerce.Ecommerce.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)//declare member variable as private
public class SellerProfileDto {

  Long id;
  String firstName;
  String lastName;
  String middleName;
  String contact;
  String email;
  boolean active;
  String imagePath;
  List<AddressDto> addressDto;


}
