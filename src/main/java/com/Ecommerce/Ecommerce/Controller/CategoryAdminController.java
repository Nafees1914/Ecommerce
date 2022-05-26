package com.Ecommerce.Ecommerce.Controller;

import com.Ecommerce.Ecommerce.Dto.Request.AddCategoryMetaDataFieldValueDTO;
import com.Ecommerce.Ecommerce.Dto.Request.UpdateCategoryMetaValueDto;
import com.Ecommerce.Ecommerce.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/category/admin")
public class CategoryAdminController
{

  @Autowired
  CategoryService categoryService;


  /*
    //127.0.0.1:8080/category/admin/add-category?categoryName=Fashion
  */
  @PostMapping("/add-category")
  public ResponseEntity<?> addCategory(@RequestParam("categoryName") String categoryName,
      @RequestParam(required = false, value = "parentId") Long parentCategoryId) {
    return categoryService.addCategory(categoryName, parentCategoryId);
  }


  /*
    //127.0.0.1:8080/category/admin/add-category/Metadata-field?fieldName=Colour
  */
  @PostMapping("/add-category/Metadata-field")
  public ResponseEntity<?> addCategoryMetadataField(@RequestParam("fieldName") String fieldName) {
    return categoryService.addMetadataField(fieldName);
  }


  /*
    //127.0.0.1:8080/category/admin/add/metadata/values/2/1?categoryId=2&metaDataFieldId=1
  */
  @PostMapping("add/metadata/values/{categoryId}/{metaDataFieldId}")
  public ResponseEntity<?> addMetaFieldValues(
      @RequestBody AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO,
      @PathVariable Long categoryId, @PathVariable Long metaDataFieldId) {
    return categoryService.addMetaDataFieldValue(addCategoryMetaDataFieldValueDTO, categoryId,
        metaDataFieldId);
  }

  /*
    //127.0.0.1:8080/category/admin/view/metadatafield
  */
  @GetMapping("/view/metadatafield")
  public ResponseEntity<?> viewMetadataField() {
    return categoryService.viewMetadataField();
  }

  /*
    //127.0.0.1:8080/category/admin/update/category?categoryId=2&categoryName=Attire
  */
  @PutMapping("/update/category")
  public ResponseEntity<?> updateCategory(@RequestParam("categoryId") Long categoryId,
      @RequestParam("categoryName") String categoryName) {
    return categoryService.updateCategory(categoryId, categoryName);
  }


  @PutMapping("/update/category-metadata-field-values/{categoryId}/{metaDataFieldId}")
  public ResponseEntity<?> updateCategoryMetadataFieldValues(
      @RequestBody UpdateCategoryMetaValueDto updateCategoryMetaValueDto,
      @PathVariable Long categoryId, @PathVariable Long metaDataFieldId) {
    return categoryService.updateCategoryMetadataFieldValues(updateCategoryMetaValueDto, categoryId,
        metaDataFieldId);
  }

  /*
    //127.0.0.1:8080/category/admin/all/categories
  */
  @GetMapping("/all/categories")
  public ResponseEntity<?> allCategories()
  {
   return categoryService.viewAllCategories();
 }

  @GetMapping("/list-all-categories/{id}")
  public ResponseEntity<?> listCategories(@PathVariable Long id)
  {
    return categoryService.viewCategoriesByOptionalId(id);
  }


}
