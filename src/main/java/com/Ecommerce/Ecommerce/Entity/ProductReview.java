package com.Ecommerce.Ecommerce.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class ProductReview   {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String review;
  private Integer rating;

  @OneToOne
  private Customer customer;

  @ManyToOne
  private Product product;
}