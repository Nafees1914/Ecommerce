package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Dto.ProductViewDto;
import com.Ecommerce.Ecommerce.Service.ProductService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin-product")
public class ProductAdminController {

  @Autowired
  ProductService productService;

  @GetMapping("/view-specific-product/{id}")
  public ResponseEntity<ProductViewDto> viewAdminProduct(@PathVariable Integer id){
    ProductViewDto response = productService.viewSpecificProduct(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @GetMapping("/view-all-product")
  public ResponseEntity<List<ProductViewDto>> viewAdminProducts() throws JSONException {
    List<ProductViewDto> response = productService.viewAllProducts();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/activate/{id}")
  public ResponseEntity<String> activateProduct(@PathVariable Integer id){
    String response = productService.activateProduct(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("/deactivate/{id}")
  public ResponseEntity<String> deactivateProduct(@PathVariable Integer id){
    String response = productService.deactivateProduct(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }



}
