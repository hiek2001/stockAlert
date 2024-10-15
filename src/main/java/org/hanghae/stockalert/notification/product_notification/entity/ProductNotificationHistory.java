package org.hanghae.stockalert.notification.product_notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "productNotificationHistory")
public class ProductNotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private int restock_rounds;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.IN_PROGRESS;

    private Long lastSentUserId; // 마지막 발송 유저 아이디

    public ProductNotificationHistory(Long productId, int restockRounds, NotificationStatus notificationStatus) {
        this.productId = productId;
        this.restock_rounds = restockRounds;
        this.status = notificationStatus;
    }

    public ProductNotificationHistory(NotificationStatus status, Long userId) {
        this.status = status;
        this.lastSentUserId = userId;
    }
}
