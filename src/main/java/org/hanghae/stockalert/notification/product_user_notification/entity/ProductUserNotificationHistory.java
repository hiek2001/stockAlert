package org.hanghae.stockalert.notification.product_user_notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "productUserNotificationHistory")
public class ProductUserNotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;

    private int restock_rounds;

    private LocalDateTime sentAt;

    public ProductUserNotificationHistory(Long userId, Long productId, int restockRounds) {
        this.userId = userId;
        this.productId = productId;
        this.restock_rounds = restockRounds;
        this.sentAt = LocalDateTime.now();
    }
}
