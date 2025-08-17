package com.challenge.idm.infrastructure.web.mapper;

import com.challenge.idm.domain.entity.Order;
import com.challenge.idm.infrastructure.web.dto.OrderRequestDto;
import com.challenge.idm.infrastructure.web.dto.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "product", target = "product")
    OrderResponseDto toResponseDto(Order order);
    Order toEntity(OrderRequestDto orderRequest);
}
