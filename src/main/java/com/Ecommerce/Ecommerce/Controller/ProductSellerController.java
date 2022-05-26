package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Dto.AddProductDto;
import com.Ecommerce.Ecommerce.Dto.ProductVariationDto;
import com.Ecommerce.Ecommerce.Dto.ProductViewDto;
import com.Ecommerce.Ecommerce.Dto.UpdateProductDto;
import com.Ecommerce.Ecommerce.Service.ProductService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/seller-product")
public class ProductSellerController {

  @Autowired
  ProductService productService;

  @PostMapping("/add-product")
  public ResponseEntity<String> addProduct(Authentication authentication,
      @Valid @RequestBody AddProductDto addProductDto) {
    String response = productService.addProduct(authentication, addProductDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/view")
  public ResponseEntity<List<ProductViewDto>> fetchAllProducts(Authentication authentication) {
    List<ProductViewDto> response = productService.viewAllProducts(authentication);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/specific-product/{id}")
  public ResponseEntity<ProductViewDto> fetchProduct(@PathVariable Integer id,
      Authentication authentication) {
    ProductViewDto productResponseDTO = productService.viewProduct(authentication, id);
    return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
  }

  @DeleteMapping("/delete/product/{id}")
  public ResponseEntity<String> removeProduct( Authentication authentication,@PathVariable Integer id){
    String response = productService.productDeletion(authentication,id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  @PatchMapping("update/product")
  public ResponseEntity<String> updateProduct(Authentication authentication, @RequestParam Integer productId, @RequestBody UpdateProductDto updateProductDto){
    String userName = authentication.getName();
    return new ResponseEntity<>(productService.productUpdate(userName,productId,
        updateProductDto),HttpStatus.OK);
  }
  @PostMapping(path ="/add-variation")
  public ResponseEntity<?> addProductVariation( @RequestBody ProductVariationDto productVariationDto)
      throws JSONException {
    return productService.createProductVariation(productVariationDto);
  }





}










