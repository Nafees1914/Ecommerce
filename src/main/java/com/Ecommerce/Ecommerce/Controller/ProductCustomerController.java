package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Dto.ProductViewDto;
import com.Ecommerce.Ecommerce.Entity.Product;
import com.Ecommerce.Ecommerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer-product")
public class ProductCustomerController {

  @Autowired
  ProductService productService;

  @GetMapping("/customer/{id}")
  public ResponseEntity<ProductViewDto> viewCustomerProduct(@PathVariable Integer id){
    ProductViewDto response = productService.viewCustomerProduct(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  @GetMapping("/customer/view/all-products/{categoryId}")
  public ResponseEntity<List<ProductViewDto>> viewAllCustomerProducts(@PathVariable Long categoryId){
    List<ProductViewDto> response = productService.customerViewAllProducts(categoryId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  @GetMapping("/customer/similar/{id}")
  public ResponseEntity<List<Product>> viewSimilarProducts(@PathVariable Integer productId) {
    List<Product> response = productService.viewSimilarProducts(productId);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
