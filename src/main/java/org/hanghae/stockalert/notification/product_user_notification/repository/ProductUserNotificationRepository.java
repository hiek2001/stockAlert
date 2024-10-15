package org.hanghae.stockalert.notification.product_user_notification.repository;

import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {

    @Query("SELECT pun.userId FROM ProductUserNotification pun WHERE pun.productId = :productId Order by pun.updatedAt DESC")
    List<Long> findUserIdByProductIdOrderByUpdatedAtDesc(Long productId);
}
