package com.challenge.idm.application.service.impl;

import com.challenge.idm.application.service.OrderService;
import com.challenge.idm.domain.entity.Order;
import com.challenge.idm.domain.exception.OrderNotFoundException;
import com.challenge.idm.infrastructure.webClient.ProductClient;
import com.challenge.idm.infrastructure.web.dto.OrderRequestDto;
import com.challenge.idm.infrastructure.web.dto.OrderResponseDto;
import com.challenge.idm.infrastructure.web.mapper.OrderMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
@Service
public class OrderServiceImpl implements OrderService {

    private final CopyOnWriteArrayList<Order> orders = new CopyOnWriteArrayList<>();
    private final OrderMapper orderMapper;
    private final ProductClient productClient;

    public OrderServiceImpl(OrderMapper orderMapper, ProductClient productClient) {
        this.orderMapper = orderMapper;
        this.productClient = productClient;
    }

    @Override
    public Mono<OrderResponseDto> createOrder(OrderRequestDto request) {
        return productClient.getProductById(request.productId())
                .map(product -> {
                    Order order = Optional.ofNullable(orderMapper.toEntity(request))
                            .orElseThrow(() -> new OrderNotFoundException("Failed to map order"));
                    order.setId(UUID.randomUUID().toString());
                    order.setProduct(product);
                    return order;
                })
                .doOnNext(order -> orders.add(order))
                .map(orderMapper::toResponseDto)
                .onErrorResume(ex -> {
                    log.error("Error in createOrder: {}", ex.getMessage());
                    return Mono.error(new OrderNotFoundException(ex.getMessage()));
                });
    }

    @Override
    public Flux<OrderResponseDto> getAllOrders() {
        return Flux.fromIterable(orders)
                .map(order -> Optional.ofNullable(order)
                        .map(orderMapper::toResponseDto)
                        .orElse(null))
                .filter(dto -> dto != null)
                .switchIfEmpty(Flux.error(new OrderNotFoundException("No orders available")));
    }

    public Flux<OrderResponseDto> getOnlineOrders() {
        return Flux.fromIterable(orders)
                .map(Optional::ofNullable)
                .filter(opt -> opt.map(order -> "ONLINE".equalsIgnoreCase(order.getTypeOrder())).orElse(false))
                .map(opt -> orderMapper.toResponseDto(opt.get()))
                .switchIfEmpty(Flux.error(new OrderNotFoundException("No online orders available")));
    }
}