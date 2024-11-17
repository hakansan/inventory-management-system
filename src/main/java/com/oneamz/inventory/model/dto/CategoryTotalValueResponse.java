package com.oneamz.inventory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CategoryTotalValueResponse {

    private Long categoryId;
    private String categoryName;
    private BigDecimal totalValue;
}