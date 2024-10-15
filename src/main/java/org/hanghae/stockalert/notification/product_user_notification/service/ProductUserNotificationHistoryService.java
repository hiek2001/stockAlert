package org.hanghae.stockalert.notification.product_user_notification.service;

import lombok.AllArgsConstructor;
import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotificationHistory;
import org.hanghae.stockalert.notification.product_user_notification.repository.ProductUserNotificationHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductUserNotificationHistoryService {

    private final ProductUserNotificationHistoryRepository repository;

    public void saveUserNotificationHistory(Long userId, Long productId, int restockRounds) {
        repository.save(new ProductUserNotificationHistory(userId, productId, restockRounds));
    }
}
