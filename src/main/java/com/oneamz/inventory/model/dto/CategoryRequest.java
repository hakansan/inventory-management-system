package com.oneamz.inventory.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "Category name cannot be blank")
    private String name;
}
