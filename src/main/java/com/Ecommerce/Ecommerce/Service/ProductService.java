package com.Ecommerce.Ecommerce.Service;

import com.Ecommerce.Ecommerce.Dto.*;
import com.Ecommerce.Ecommerce.Entity.*;
import com.Ecommerce.Ecommerce.Exception.CategoryNotFoundException;
import com.Ecommerce.Ecommerce.Exception.UserNotFoundException;
import com.Ecommerce.Ecommerce.Repository.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Transactional
public class ProductService {

  @Autowired
  ProductRepository productRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  SellerRepository sellerRepository;
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  CategoryMetaDataFieldValuesRepository categoryMetaDataFieldValuesRepository;
  @Autowired
  ProductVariationRepository productVariationRepository;
  @Autowired
  MailSender mailSender;

  public String addProduct(Authentication authentication, AddProductDto addProductDto) {
    User user = (User) authentication.getPrincipal();
    Optional<Seller> sellerData = sellerRepository.findByEmail(user.getUsername());
    if (sellerData.isPresent()) {
      Seller seller = sellerData.get();
      SellerProfileDto sellerProfileDto = new SellerProfileDto();
      String brandName = addProductDto.getBrand();
      Category associatedCategory = categoryRepository.findById(
              (long) addProductDto.getCategoryId())
          .orElseThrow(() -> new CategoryNotFoundException("Not found"));
      if (!associatedCategory.getSubCategory().isEmpty()) {
        throw new CategoryNotFoundException("Category Not found");
      }
      String productName = addProductDto.getName();
      String productDescription = addProductDto.getDescription();
      Optional<Product> existingProduct
          = productRepository.findByNameAndSellerAndCategoryAndBrand(
          productName, seller, associatedCategory, brandName
      );
      if (existingProduct.isPresent()) {
        throw new CategoryNotFoundException("Product already present");
      }
      // save product
      Product product = new Product();
      product.set_active(false);
      product.setIs_cancellable(false);
      product.setIs_returnable(false);
      product.setName(productName);
      product.setDescription(productDescription);
      product.setBrand(brandName);
      product.setCategory(associatedCategory);
      product.setSeller(seller);
      productRepository.save(product);
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom("nafeesahmad1914@gmail.com");
      mailMessage.setTo(user.getUsername());
      mailMessage.setSubject("Password Changed");
      mailMessage.setText("ALERT!, Your account's password has been changed, If it was not you contact Admin asap.\nStay Safe, Thanks.");
      mailMessage.setSentDate(new Date());

      try
      {
        mailSender.send(mailMessage);
      }
      catch (MailException e)
      {
        log.info("Error sending mail");
      }
    } else {
      throw new CategoryNotFoundException("Category does not exists");
    }

    return "Product added successfully";
  }

  public List<ProductViewDto> viewAllProducts(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    Optional<Seller> sellerData = sellerRepository.findByEmail(user.getUsername());
    if (sellerData.isPresent()) {
      Seller seller = sellerData.get();
      List<Product> products = productRepository.findBySeller(seller);
      if (products.isEmpty()) {
        throw new CategoryNotFoundException("product not found");
      }
      List<ProductViewDto> productResponseDTOList = new ArrayList<>();
      for (Product product : products) {
        ProductViewDto productViewDto = new ProductViewDto();
        productViewDto.setId(product.getId());
        productViewDto.setName(product.getName());
        productViewDto.setBrand(product.getBrand());
        productViewDto.setDescription(product.getDescription());
        productViewDto.setIsActive(product.is_active());
        productViewDto.setIsReturnable(product.getIs_returnable());
        productViewDto.setIsCancellable(product.getIs_cancellable());
        productViewDto.setCategory(product.getCategory());
        productResponseDTOList.add(productViewDto);

      }
      return productResponseDTOList;
    }
    return null;
  }

  public ProductViewDto viewProduct(Authentication authentication, Integer id){
    Optional<Product> product = productRepository.findById(id);
    if (product == null || product.isEmpty()) {
      throw new CategoryNotFoundException("Product Invalid");
    }
    Optional<com.Ecommerce.Ecommerce.Entity.User> user = userRepository.findByEmail(authentication.getName());
    if(product.get().getSeller().getId() == user.get().getId()){
      ProductViewDto productViewDto = new ProductViewDto();
      productViewDto.setId(product.get().getId());
      productViewDto.setName(product.get().getName());
      productViewDto.setBrand(product.get().getBrand());
      productViewDto.setDescription(product.get().getDescription());
      productViewDto.setIsActive(product.get().is_active());
      productViewDto.setIsReturnable(product.get().getIs_returnable());
      productViewDto.setIsCancellable(product.get().getIs_cancellable());
      productViewDto.setCategory(product.get().getCategory());
      return productViewDto;
    } else {
      throw new CategoryNotFoundException("api.error.sellerNotAssociated");
    }
  }
  public String productDeletion(Authentication authentication, Integer id){
    Optional<Product> product = productRepository.findById(id);
    if (product == null || product.isEmpty()) {
      throw new CategoryNotFoundException("api.error.invalidProductId");
    }
    User user = (User) authentication.getPrincipal();
    Optional<Seller> sellerData = sellerRepository.findByEmail(user.getUsername());
    if (sellerData.isPresent()) {
      Seller seller = sellerData.get();
      productRepository.delete(product.get());
      return "Successfully deleted";
    } else{
      throw new CategoryNotFoundException("Failed to delete the product");
    }

  }

  public String productUpdate(String sellerEmail, Integer prodId, UpdateProductDto updateProductDto){
    Product savedProduct=productRepository.findById(prodId).orElseThrow(
        ()-> new CategoryNotFoundException("Product does not exists with this id:"+prodId));

    Optional<Seller> seller=sellerRepository.findByEmail(sellerEmail);
    if(!seller.isPresent())
      throw new CategoryNotFoundException("Seller not found");
    boolean isProductExists = savedProduct.getSeller().getId()==seller.get().getId();
    if(!isProductExists){
      throw new CategoryNotFoundException("Only Seller has access to product");
    }
    if (updateProductDto.getName() != null)
      savedProduct.setName(updateProductDto.getName());
    if (updateProductDto.getBrand() != null)
      savedProduct.setBrand(updateProductDto.getBrand());
    if (updateProductDto.getDescription() != null)
      savedProduct.setDescription(updateProductDto.getDescription());
    if (updateProductDto.getIsCancellable() != null)
      savedProduct.setIs_cancellable(updateProductDto.getIsCancellable());
    if (updateProductDto.getIsReturnable() != null)
      savedProduct.setIs_returnable(updateProductDto.getIsReturnable());
    return "Product Updated Successfully";
  }

  public ResponseEntity<?> createProductVariation(ProductVariationDto productVariationDto)
      throws JSONException {

    ProductVariation productVariation = new ProductVariation();
    Product product =   productRepository.findById(productVariationDto.getProductId())
        .orElseThrow(()-> new CategoryNotFoundException("Product Not Found For This Id"));

    if(!product.is_active() || product.is_deleted()){
      return new ResponseEntity<>("Product is not Active Or Product is deleted", HttpStatus.BAD_REQUEST);
    }

    Category category = product.getCategory();
    List<CategoryMetaDataFieldValue> categoryMetadataFieldValueList=
        categoryMetaDataFieldValuesRepository.findByCategoryId(category.getId());
    Map<Object,Set<String>> meta = new LinkedHashMap<>();

    for(CategoryMetaDataFieldValue categoryMetadataFieldValue : categoryMetadataFieldValueList) {
      String[] values =  categoryMetadataFieldValue.getValue().split(",");
      List<String> list = Arrays.asList(values);
      Set<String> listSet = new HashSet<>(list);

      meta.put(categoryMetadataFieldValue.getCategoryMetaDataField().getName(),
          listSet);

    }
    String metadata = productVariationDto.getMetaData();
    JSONObject jsonObj = new JSONObject(metadata);
    Iterator keys = jsonObj.keys();

    while(keys.hasNext()){
      String currentKey = (String)keys.next();
      if (meta.get(currentKey) == null){
        return new ResponseEntity<>("metadata value mismatch",HttpStatus.BAD_REQUEST);
      }
      if (!meta.get(currentKey).contains(jsonObj.getString(currentKey))){

        return new ResponseEntity<>("invalid value in metadata field",HttpStatus.BAD_REQUEST);
      }
    }
    productVariation.setPrice(productVariation.getPrice());
    productVariation.setProduct(product);
    productVariation.setMetadata(jsonObj.toString());
    productVariation.setQuantityAvailable(productVariationDto.getQuantity());
    productVariation.setIsActive(true);

    productVariationRepository.save(productVariation);
    return new ResponseEntity<>("product variation created successfully",HttpStatus.CREATED);
  }


  public ProductViewDto viewCustomerProduct(Integer productId){
    Optional<Product> product = productRepository.findById(productId);
    if (product == null || product.isEmpty()) {
      throw new CategoryNotFoundException("Product is not Activated or deleted");
    }
    // check product status
    if (product.get().is_deleted() || product.get().is_deleted() == false ){
      throw new CategoryNotFoundException("Inactive Product");
    }
    ProductViewDto productViewDto=new ProductViewDto();
    productViewDto.setId(product.get().getId());
    productViewDto.setName(product.get().getName());
    productViewDto.setBrand(product.get().getBrand());
    productViewDto.setDescription(product.get().getDescription());
    productViewDto.setIsActive(product.get().is_active());
    productViewDto.setIsReturnable(product.get().getIs_returnable());
    productViewDto.setIsCancellable(product.get().getIs_cancellable());
    productViewDto.setCategory(product.get().getCategory());

    return productViewDto;
  }




  }











