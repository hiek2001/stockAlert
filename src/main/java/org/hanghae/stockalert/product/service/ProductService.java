package org.hanghae.stockalert.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanghae.stockalert.notification.product_notification.controller.NotificationManager;
import org.hanghae.stockalert.notification.product_notification.dto.NotificationRequestDto;
import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.hanghae.stockalert.notification.product_notification.service.CacheService;
import org.hanghae.stockalert.notification.product_notification.service.ProductNotificationHistoryService;
import org.hanghae.stockalert.notification.product_user_notification.service.ProductUserNotificationHistoryService;
import org.hanghae.stockalert.notification.product_user_notification.service.UserNotificationService;
import org.hanghae.stockalert.product.entity.Product;
import org.hanghae.stockalert.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final UserNotificationService userNotificationService;
    private final ProductNotificationHistoryService notificationHistoryService;
    private final ProductUserNotificationHistoryService userNotificationHistoryService;
    private final CacheService cacheService;

    private final NotificationManager notificationManager;

    public void restockProduct(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("Product not found"));

        // 상품 재고 횟수 및 수량 증가
        product.reStock(quantity);

        // 상품별 재입고 알림 히스토리 저장
        notificationHistoryService.saveProductHistory(productId, product.getRestock_rounds(), NotificationStatus.IN_PROGRESS);

        // 재입고 신청한 유저 목록 가져오기
        List<Long> userIdNotifications = userNotificationService.getUserNotifications(productId);

        NotificationRequestDto requestDto = new NotificationRequestDto(productId, userIdNotifications);
        notificationManager.notifyRestock(requestDto);
    }

    @Transactional
    public void sellProduct(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("Product not found"));

        product.sell(quantity);
    }

}
