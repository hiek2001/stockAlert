package org.hanghae.stockalert.notification.product_notification.dto;

import lombok.Getter;
import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;


import java.util.List;

@Getter
public class NotificationRequestDto {
    private Long productId;
    private List<Long> users;
    private NotificationStatus status;

    public NotificationRequestDto(Long productId, List<Long> users, NotificationStatus status) {
        this.productId = productId;
        this.users = users;
        this.status = status;
    }
}
