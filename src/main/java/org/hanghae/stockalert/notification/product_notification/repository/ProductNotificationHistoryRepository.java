package org.hanghae.stockalert.notification.product_notification.repository;

import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.hanghae.stockalert.notification.product_notification.entity.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
    ProductNotificationHistory findByProductId(Long productId);

    NotificationStatus findStatusByProductId(Long productId);
}
