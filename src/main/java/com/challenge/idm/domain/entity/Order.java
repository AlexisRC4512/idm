package com.challenge.idm.domain.entity;

import com.challenge.idm.infrastructure.web.dto.ProductResponseDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private String id;
    private String name;
    private String productId;
    private String description;
    private String typeOrder;
    private int quantity;
    private ProductResponseDto product;
}
