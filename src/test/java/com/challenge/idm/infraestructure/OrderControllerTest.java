package com.challenge.idm.infraestructure;

import com.challenge.idm.application.service.OrderService;
import com.challenge.idm.infrastructure.controller.OrderController;
import com.challenge.idm.infrastructure.web.dto.OrderRequestDto;
import com.challenge.idm.infrastructure.web.dto.OrderResponseDto;
import com.challenge.idm.infrastructure.web.dto.ProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderRequestDto requestDto;
    private OrderResponseDto responseDto;
    private ProductResponseDto productResponse;

    @BeforeEach
    void setup() {
        productResponse = new ProductResponseDto("1", "Laptop", 23.9);
        requestDto = new OrderRequestDto(
                "Laptop Order", "1", "Dell XPS 13", "ONLINE", 2
        );
        responseDto = new OrderResponseDto(
                "order-123", "Laptop Order", "Dell XPS 13", "ONLINE", 2, productResponse
        );
    }

    @Test
    void createOrderController_success() {
        when(orderService.createOrder(requestDto)).thenReturn(Mono.just(responseDto));

        StepVerifier.create(orderController.createOrder(requestDto))
                .expectNextMatches(res -> res.getBody().equals(responseDto))
                .verifyComplete();

        verify(orderService).createOrder(requestDto);
    }

    @Test
    void getAllOrdersController_success() {
        when(orderService.getAllOrders()).thenReturn(Flux.just(responseDto));

        StepVerifier.create(orderController.getAllOrders())
                .expectNext(responseDto)
                .verifyComplete();

        verify(orderService).getAllOrders();
    }
}
