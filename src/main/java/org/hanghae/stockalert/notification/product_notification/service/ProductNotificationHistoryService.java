package org.hanghae.stockalert.notification.product_notification.service;

import lombok.AllArgsConstructor;
import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.hanghae.stockalert.notification.product_notification.entity.ProductNotificationHistory;
import org.hanghae.stockalert.notification.product_notification.repository.ProductNotificationHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductNotificationHistoryService {

    private final ProductNotificationHistoryRepository productNotificationHistoryRepository;

    public ProductNotificationHistory getProductNotificationHistory(Long productId) {
        return productNotificationHistoryRepository.findByProductId(productId);
    }

    public NotificationStatus getStatusByProductId(Long productId) {
        return productNotificationHistoryRepository.findStatusByProductId(productId);
    }

    public void saveProductHistory(Long productId, int restockRounds, NotificationStatus notificationStatus) {
        productNotificationHistoryRepository.save(new ProductNotificationHistory(
                productId,
                restockRounds,
                notificationStatus)
        );
    }
}
