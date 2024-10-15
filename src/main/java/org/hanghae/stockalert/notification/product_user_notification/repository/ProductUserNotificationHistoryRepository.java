package org.hanghae.stockalert.notification.product_user_notification.repository;

import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductUserNotificationHistoryRepository extends JpaRepository<ProductUserNotificationHistory, Long> {
}
