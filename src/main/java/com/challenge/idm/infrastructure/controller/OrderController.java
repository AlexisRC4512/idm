package com.challenge.idm.infrastructure.controller;

import com.challenge.idm.application.service.OrderService;
import com.challenge.idm.infrastructure.web.dto.OrderRequestDto;
import com.challenge.idm.infrastructure.web.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new order with the specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public Mono<ResponseEntity<OrderResponseDto>> createOrder(
            @Valid @RequestBody OrderRequestDto request) {
        return orderService.createOrder(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    public Flux<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }
}