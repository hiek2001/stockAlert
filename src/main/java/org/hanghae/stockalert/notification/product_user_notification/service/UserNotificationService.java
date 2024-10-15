package org.hanghae.stockalert.notification.product_user_notification.service;

import lombok.AllArgsConstructor;
import org.hanghae.stockalert.notification.product_user_notification.repository.ProductUserNotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserNotificationService {

    private ProductUserNotificationRepository userNotificationRepository;

    public List<Long> getUserNotifications(Long productId) {
        return userNotificationRepository.findUserIdByProductIdOrderByUpdatedAtDesc(productId);
    }
}
