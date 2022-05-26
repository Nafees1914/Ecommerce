package com.Ecommerce.Ecommerce.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Product  {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "product_id")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "seller_user_id")
  private Seller seller;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  private String description;

  @NotNull(message = "Name should not be blank")
  private String name;

  private Boolean is_cancellable;
  private Boolean is_returnable;
  private String brand;
  private boolean is_active;
  private boolean is_deleted;

  @OneToMany(mappedBy = "product")
  private List<ProductVariation> productVariations;


  @OneToMany(mappedBy = "product")
  private List<ProductReview> productReviews;


}
