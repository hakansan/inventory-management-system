package com.oneamz.inventory.service;

import com.oneamz.inventory.exception.ApiResponseCode;
import com.oneamz.inventory.exception.InventoryException;
import com.oneamz.inventory.model.dto.CategoryResponse;
import com.oneamz.inventory.model.dto.CategoryTotalValueResponse;
import com.oneamz.inventory.model.dto.ProductRequest;
import com.oneamz.inventory.model.dto.ProductResponse;
import com.oneamz.inventory.model.entity.Category;
import com.oneamz.inventory.model.entity.Discount;
import com.oneamz.inventory.model.entity.Product;
import com.oneamz.inventory.repository.CategoryRepository;
import com.oneamz.inventory.repository.DiscountRepository;
import com.oneamz.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final DiscountRepository discountRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Retrieving all products");
        return productRepository.findByActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Retrieving product with ID: {}", id);
        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", id);
                    return new InventoryException(ApiResponseCode.PRODUCT_NOT_FOUND);
                });
        return convertToResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Creating new product with name: {}", productRequest.getName());
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category with ID {} not found", productRequest.getCategoryId());
                    return new InventoryException(ApiResponseCode.CATEGORY_NOT_FOUND);
                });

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(category);
        product.setActive(true);
        product.setDiscounted(false);

        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        return convertToResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        log.info("Updating product with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", id);
                    return new InventoryException(ApiResponseCode.PRODUCT_NOT_FOUND);
                });

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category with ID {} not found", productRequest.getCategoryId());
                    return new InventoryException(ApiResponseCode.CATEGORY_NOT_FOUND);
                });

        existingProduct.setName(productRequest.getName());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setQuantity(productRequest.getQuantity());
        existingProduct.setCategory(category);

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product with ID {} updated successfully", updatedProduct.getId());
        return convertToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", id);
                    return new InventoryException(ApiResponseCode.PRODUCT_NOT_FOUND);
                });

        existingProduct.setActive(false);
        productRepository.save(existingProduct);

        List<Discount> activeDiscounts = discountRepository.findByProductIdAndActiveTrue(id);
        for (Discount activeDiscount : activeDiscounts) {
            activeDiscount.setActive(false);
            discountRepository.save(activeDiscount);
        }
        log.info("Product with ID {} marked as inactive", id);
    }

    @Override
    @Transactional
    public ProductResponse applyDiscount(Long productId, BigDecimal discountRate) {
        log.info("Applying discount of {}% to product with ID: {}", discountRate, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InventoryException(ApiResponseCode.PRODUCT_NOT_FOUND));

        try {
            if (discountRate.compareTo(BigDecimal.ZERO) <= 0 || discountRate.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new InventoryException(ApiResponseCode.INVALID_REQUEST, "Discount percentage must be between 0 and 100");
            }

            BigDecimal discountAmount = product.getPrice().multiply(discountRate).divide(BigDecimal.valueOf(100));
            BigDecimal discountedPrice = product.getPrice().subtract(discountAmount);

            List<Discount> activeDiscounts = discountRepository.findByProductIdAndActiveTrue(product.getId());
            for (Discount activeDiscount : activeDiscounts) {
                activeDiscount.setActive(false);
                discountRepository.save(activeDiscount);
            }

            Discount discount = new Discount();
            discount.setProductId(product.getId());
            discount.setDiscountRate(discountRate);
            discountRepository.save(discount);

            product.setDiscountedPrice(discountedPrice);
            product.setDiscounted(true);

            Product updatedProduct = productRepository.save(product);

            log.info("Discount applied to product with ID: {}", updatedProduct.getId());
            return convertToResponse(updatedProduct);

        } catch (ArithmeticException e) {
            log.error("Error applying discount: {}", e.getMessage(), e);
            throw new InventoryException(ApiResponseCode.INTERNAL_ERROR, "Error occurred while applying discount");
        }
    }

    @Override
    @Transactional
    public ProductResponse removeDiscountsByProductId(Long productId) {
        log.info("Removing discounts from product with ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InventoryException(ApiResponseCode.PRODUCT_NOT_FOUND));

        if (!product.isDiscounted()) {
            log.warn("Product with ID {} does not have an active discount", productId);
            throw new InventoryException(ApiResponseCode.INVALID_REQUEST, "Product does not have an active discount to remove");
        }

        List<Discount> activeDiscounts = discountRepository.findByProductIdAndActiveTrue(productId);
        for (Discount activeDiscount : activeDiscounts) {
            activeDiscount.setActive(false);
            discountRepository.save(activeDiscount);
        }

        product.setDiscounted(false);
        product.setDiscountedPrice(null);
        Product updatedProduct = productRepository.save(product);

        log.info("All active discounts removed from product with ID: {}", updatedProduct.getId());
        return convertToResponse(updatedProduct);
    }

    public List<CategoryTotalValueResponse> calculateTotalValueForAllCategories() {
        log.info("Calculating total value for all categories");
        try {
            List<Category> categories = categoryRepository.findAll();

            return categories.stream()
                    .map(category -> {
                        List<Product> products = productRepository.findByCategoryAndActiveTrue(category);

                        BigDecimal totalValue = products.stream()
                                .map(product -> {
                                    BigDecimal price = product.isDiscounted() ? product.getDiscountedPrice() : product.getPrice();
                                    return price.multiply(BigDecimal.valueOf(product.getQuantity()));
                                })
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        return new CategoryTotalValueResponse(category.getId(), category.getName(), totalValue);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("An error occurred while calculating total values for categories: {}", e.getMessage(), e);
            throw new InventoryException(ApiResponseCode.INTERNAL_ERROR, "Error occurred while calculating total values for categories");
        }
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setDiscountedPrice(product.getDiscountedPrice());
        dto.setDiscounted(product.isDiscounted());
        dto.setActive(product.isActive());

        if (product.getCategory() != null) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(product.getCategory().getId());
            categoryResponse.setName(product.getCategory().getName());
            dto.setCategoryResponse(categoryResponse);
        }

        return dto;
    }
}