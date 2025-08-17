package com.challenge.idm.application;
import com.challenge.idm.application.service.impl.OrderServiceImpl;
import com.challenge.idm.domain.entity.Order;
import com.challenge.idm.domain.exception.OrderNotFoundException;
import com.challenge.idm.infrastructure.webClient.ProductClient;
import com.challenge.idm.infrastructure.web.dto.OrderRequestDto;
import com.challenge.idm.infrastructure.web.dto.OrderResponseDto;
import com.challenge.idm.infrastructure.web.dto.ProductResponseDto;
import com.challenge.idm.infrastructure.web.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDto requestDto;
    private ProductResponseDto productResponse;
    private Order order;
    private OrderResponseDto responseDto;

    @BeforeEach
    void setup() {
        requestDto = new OrderRequestDto("1", "Laptop Order", "Dell XPS 13", "ONLINE", 2);
        productResponse = new ProductResponseDto("1", "Laptop", 23.9);

        order = Order.builder()
                .id("order-123")
                .name("Laptop Order")
                .productId("1")
                .description("Dell XPS 13")
                .typeOrder("ONLINE")
                .quantity(2)
                .build();

        responseDto = new OrderResponseDto(
                order.getId(),
                order.getName(),
                order.getDescription(),
                order.getTypeOrder(),
                order.getQuantity(),
                productResponse
        );
    }


    @Test
    void createOrder_error_whenProductNotFound() {
        requestDto = new OrderRequestDto(
                "Laptop Order",
                "1",
                "Dell XPS 13",
                "ONLINE",
                2
        );

        when(productClient.getProductById("1"))
                .thenReturn(Mono.error(new RuntimeException("Product not found")));

        Mono<OrderResponseDto> result = orderService.createOrder(requestDto);

        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof OrderNotFoundException &&
                                ex.getMessage().contains("Product not found"))
                .verify();

        verify(productClient).getProductById("1");
        verify(orderMapper, never()).toEntity(any());
    }

    @Test
    void createOrderSuccess() {
        OrderRequestDto requestDto = new OrderRequestDto(
                "Laptop Order",
                "1",
                "Dell XPS 13",
                "ONLINE",
                2
        );
        ProductResponseDto productResponse = new ProductResponseDto(
                "1",
                "Laptop Pro",
                1500.75
        );
        when(productClient.getProductById(anyString())).thenReturn(Mono.just(productResponse));

        when(orderMapper.toEntity(any(OrderRequestDto.class))).thenAnswer(invocation -> {
            Order order = new Order();
            OrderRequestDto req = invocation.getArgument(0);
            order.setName(req.name());
            order.setDescription(req.description());
            order.setTypeOrder(req.typeOrder());
            order.setQuantity(req.quantity());
            return order;
        });

        when(orderMapper.toResponseDto(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            ProductResponseDto prodDto = new ProductResponseDto(
                    order.getProduct().id(),
                    order.getProduct().name(),
                    order.getProduct().price()
            );
            return new OrderResponseDto(
                    order.getId(),
                    order.getName(),
                    order.getDescription(),
                    order.getTypeOrder(),
                    order.getQuantity(),
                    prodDto
            );
        });
        Mono<OrderResponseDto> result = orderService.createOrder(requestDto);

        StepVerifier.create(result)
                .expectNextMatches(res ->
                        res.id() != null &&
                                res.name().equals("Laptop Order") &&
                                res.product() != null &&
                                res.product().id().equals("1") &&
                                res.product().name().equals("Laptop Pro")
                )
                .verifyComplete();
        verify(productClient).getProductById("1");
        verify(orderMapper).toEntity(requestDto);
    }

    @Test
    void getAllOrders_error_whenEmpty() {
        Flux<OrderResponseDto> result = orderService.getAllOrders();

        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof OrderNotFoundException &&
                                ex.getMessage().equals("No orders available"))
                .verify();
    }
}