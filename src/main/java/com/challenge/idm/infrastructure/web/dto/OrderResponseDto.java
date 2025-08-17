package com.challenge.idm.infrastructure.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response data of an order")
public record OrderResponseDto(
        String id,
        String name,
        String description,
        String typeOrder,
        Integer quantity,
        ProductResponseDto product
) {}
