package org.hanghae.stockalert.notification.product_notification.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae.stockalert.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final ProductService productService;

    @PostMapping("/products/{productId}/notifications/re-stock")
    public ResponseEntity<String> sendRestockNotification(@PathVariable("productId") Long productId, int quantity) {
        productService.restockProduct(productId, quantity);

        return ResponseEntity.ok("Restock notification sent to " + productId);
    }
}
