package com.oneamz.inventory.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private Integer quantity;
    private boolean isDiscounted;
    private CategoryResponse categoryResponse;
    private boolean active;
}