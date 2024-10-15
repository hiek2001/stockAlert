package org.hanghae.stockalert.notification.product_user_notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "productUserNotification")
public class ProductUserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long productId;

    @ColumnDefault("true")
    private boolean isNotificationEnabled = true; // 알림 활성화 여부

    private boolean isLast = false;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ProductUserNotification(Long productId, Object userId) {
        this.productId = productId;
        this.userId = Long.valueOf(String.valueOf(userId));
        this.isNotificationEnabled = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void endElement() {
        this.isLast = true;
    }
}
