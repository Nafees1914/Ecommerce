package com.Ecommerce.Ecommerce.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ProductVariation  {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private Integer quantityAvailable;
  private Double price;
  private String primaryImageName;
  private String metadata;
  private Boolean isActive;

}
