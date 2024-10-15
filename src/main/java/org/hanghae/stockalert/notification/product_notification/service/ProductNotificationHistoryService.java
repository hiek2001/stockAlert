package org.hanghae.stockalert.notification.product_notification.service;

import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.hanghae.stockalert.notification.product_notification.entity.ProductNotificationHistory;
import org.hanghae.stockalert.notification.product_notification.repository.ProductNotificationHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductNotificationHistoryService {

    private ProductNotificationHistoryRepository productNotificationHistoryRepository;

    public ProductNotificationHistory getProductNotificationHistory(Long productId) {
        return productNotificationHistoryRepository.findByProductId(productId);
    }

    public NotificationStatus findStatusByProductId(Long productId) {
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
