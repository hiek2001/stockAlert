package org.hanghae.stockalert.notification.product_notification.service;


import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    // 캐시에서 알림 상태를 가져오기
    @Cacheable(value="notificationStatusCode", key = "#productId")
    public NotificationStatus  getNotificationStatus(Long productId) {
        // 캐시에 없으면 null 반환
        return null;
    }

    // 알림 상태를 캐시에 저장
    @CachePut(value = "notificationStatusCache", key = "#productId")
    public NotificationStatus saveNotificationStatus(Long productId, NotificationStatus status) {
        return status;
    }

    // 알림 상태를 캐시에서 제거 (예: 알림 완료 후)
    @CacheEvict(value = "notificationStatusCache", key = "#productId")
    public void evictNotificationStatus(Long productId) {
        // 캐시에서 삭제됨
    }
}
