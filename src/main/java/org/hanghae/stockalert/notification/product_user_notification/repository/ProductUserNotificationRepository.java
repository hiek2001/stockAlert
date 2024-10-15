package org.hanghae.stockalert.notification.product_user_notification.repository;

import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    List<ProductUserNotification> findAllByProductId(Long productId);

    List<Long> findUserIdByProductId(Long productId);
}
