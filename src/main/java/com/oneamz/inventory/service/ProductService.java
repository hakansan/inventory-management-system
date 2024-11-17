package com.oneamz.inventory.service;

import com.oneamz.inventory.model.dto.CategoryTotalValueResponse;
import com.oneamz.inventory.model.dto.ProductRequest;
import com.oneamz.inventory.model.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
    ProductResponse applyDiscount(Long productId, BigDecimal discountRate);
    List<CategoryTotalValueResponse> calculateTotalValueForAllCategories();
    ProductResponse removeDiscountsByProductId(Long productId);
}
