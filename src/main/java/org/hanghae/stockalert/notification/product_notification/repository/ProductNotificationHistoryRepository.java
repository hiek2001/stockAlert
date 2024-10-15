package org.hanghae.stockalert.notification.product_notification.repository;

import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.hanghae.stockalert.notification.product_notification.entity.ProductNotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {
    ProductNotificationHistory findByProductId(Long productId);

    @Query("SELECT p.status FROM ProductNotificationHistory p WHERE p.productId = :productId")
    NotificationStatus findStatusByProductId(@Param("productId") Long productId);

    int findRestockRoundsByProductId(Long productId);
}
