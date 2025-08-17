package com.challenge.idm.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request data to create an order")
public record OrderRequestDto(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name cannot exceed 50 characters")
        @Schema(description = "Order name", example = "Laptop Order")
        String name,

        @NotBlank(message = "productId is required")
        @Size(max = 50)
        @Schema(description = "Product ID to order", example = "1")
        String productId,

        @Size(max = 200)
        @Schema(description = "Order description", example = "Order for Dell XPS 13")
        String description,

        @NotBlank
        @Schema(description = "Type of order", example = "ONLINE")
        String typeOrder,

        @NotNull
        @Min(1)
        @Schema(description = "Quantity of products", example = "2")
        Integer quantity
) {}
