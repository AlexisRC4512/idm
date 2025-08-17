package com.challenge.idm.infrastructure.webClient;

import com.challenge.idm.infrastructure.web.dto.ProductResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(WebClient.Builder builder,
                         @Value("${products.api.url}") String productsApiUrl) {
        this.webClient = builder
                .baseUrl(productsApiUrl)
                .build();
    }

    public Mono<ProductResponseDto> getProductById(String productId) {
        return webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .bodyToMono(ProductResponseDto.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found with id: " + productId)));
    }
}