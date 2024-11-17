package com.oneamz.inventory.controller;

import com.oneamz.inventory.model.dto.CategoryTotalValueResponse;
import com.oneamz.inventory.model.dto.ProductRequest;
import com.oneamz.inventory.model.dto.ProductResponse;
import com.oneamz.inventory.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Api(tags = "Product Management")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ApiOperation(value = "Get all products", notes = "Retrieves all products from the inventory")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get product by ID", notes = "Retrieves a specific product by its ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @ApiOperation(value = "Create a new product", notes = "Creates a new product in the inventory")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        ProductResponse createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED) .body(createdProduct);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a product", notes = "Updates an existing product by its ID")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a product", notes = "Deletes a specific product by its ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/discount")
    @ApiOperation(value = "Apply discount to a product", notes = "Applies a discount to a product by its ID")
    public ResponseEntity<ProductResponse> applyDiscount(@PathVariable Long productId, @RequestParam BigDecimal discountRate) {
        ProductResponse response = productService.applyDiscount(productId, discountRate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-value")
    @ApiOperation(value = "Get total values for all categories", notes = "Calculates the total value of products for each category")
    public ResponseEntity<List<CategoryTotalValueResponse>> getTotalValuesForAllCategories() {
        List<CategoryTotalValueResponse> categoryValues = productService.calculateTotalValueForAllCategories();
        return ResponseEntity.ok(categoryValues);
    }

    @DeleteMapping("/{productId}/discount")
    @ApiOperation(value = "Remove discounts from a product", notes = "Removes all discounts from a product by its ID")
    public ResponseEntity<ProductResponse> removeDiscountsByProductId(@PathVariable Long productId) {
        ProductResponse response = productService.removeDiscountsByProductId(productId);
        return ResponseEntity.ok(response);
    }
}