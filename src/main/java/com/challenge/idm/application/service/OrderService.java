package com.challenge.idm.application.service;

import com.challenge.idm.infrastructure.web.dto.OrderRequestDto;
import com.challenge.idm.infrastructure.web.dto.OrderResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponseDto> createOrder(OrderRequestDto request);
    Flux<OrderResponseDto> getAllOrders();
}
