package com.challenge.idm.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product data included in an order")
public record ProductResponseDto(
        String id,
        String name,
        Double price
) {}
