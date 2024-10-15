package org.hanghae.stockalert.notification.product_notification.service;

import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.hanghae.stockalert.notification.product_notification.entity.ProductNotificationHistory;
import org.hanghae.stockalert.notification.product_notification.repository.ProductNotificationHistoryRepository;
import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotification;
import org.hanghae.stockalert.notification.product_user_notification.service.ProductUserNotificationHistoryService;


import java.util.concurrent.BlockingDeque;


@Slf4j
public class Consumer implements Runnable {

    private final ProductNotificationHistoryRepository productHistoryRepository;

    private final ProductUserNotificationHistoryService userNotificationHistoryService;
    private final CacheService cacheService;

    private final BlockingDeque<ProductUserNotification> queue;
    private final Bucket bucket;

    private ProductUserNotification notification;
    private NotificationStatus status;

    public Consumer(BlockingDeque<ProductUserNotification> productUserNotifications, Bucket bucket, ProductUserNotificationHistoryService userNotificationHistoryService, CacheService cacheService, ProductNotificationHistoryRepository productHistoryRepository) {
        this.userNotificationHistoryService = userNotificationHistoryService;
        this.bucket = bucket;
        this.queue = productUserNotifications;
        this.cacheService = cacheService;
        this.productHistoryRepository = productHistoryRepository;
    }


    @Override
    public void run() {
        while (true) {
            try {
                notification = queue.take();

                // 캐시에서 알림 상태 가져오기
                status = cacheService.getNotificationStatus(notification.getProductId());

                // 매진 될 경우
                if(isSoldOut(status)) {
                    this.updateNotiStatusPoductHistory(NotificationStatus.CANCELED_BY_SOLD_OUT, notification.getUserId()); // DB에 저장
                    cacheService.saveNotificationStatus(notification.getProductId(), NotificationStatus.CANCELED_BY_SOLD_OUT); // 캐시에 저장
                }

                // 버킷으로 1초에 최대 500개 처리
                if(bucket.tryConsume(1) && isInProgress(status)) {

                    sendRestockNotification(notification); // 알림 전송

                    // 유저별 알림 히스토리 저장
                    int restock_rounds = productHistoryRepository.findRestockRoundsByProductId(notification.getProductId());
                    userNotificationHistoryService.saveUserNotificationHistory(notification.getUserId(), notification.getProductId(), restock_rounds);

                    // 마지막 요청일 경우
                    if(isLastNotification(notification)) {
                        this.updateNotiStatusPoductHistory(NotificationStatus.COMPLETED, notification.getUserId()); // DB에 저장
                        cacheService.saveNotificationStatus(notification.getProductId(), NotificationStatus.COMPLETED); // 캐시에 저장
                    }
                    // 토큰이 부족할 경우
                } else if(isInProgress(status)) {
                    log.info("token is empty");
                    queue.addFirst(notification);
                }

            } catch (Exception e) {
                log.error(e.getMessage());

                // 오류 발생 시
                if(isInProgress(status)) {
                    this.updateNotiStatusPoductHistory(NotificationStatus.CANCELED_BY_ERROR, notification.getUserId()); // DB에 저장
                    cacheService.saveNotificationStatus(notification.getProductId(), NotificationStatus.CANCELED_BY_ERROR); // 캐시에 저장
                }
            }
        }
    }

    private void updateNotiStatusPoductHistory(NotificationStatus status, Long userId) {
        ProductNotificationHistory history = new ProductNotificationHistory(status, userId);
        productHistoryRepository.save(history);
    }

    private boolean isSoldOut(NotificationStatus status) {
        return status.equals(NotificationStatus.CANCELED_BY_SOLD_OUT);
    }

    private boolean isInProgress(NotificationStatus status) {
        return status.equals(NotificationStatus.IN_PROGRESS);
    }

    private boolean isLastNotification(ProductUserNotification notification) {
        return notification.isLast();
    }

    private void sendRestockNotification(ProductUserNotification notification) {
        log.info("알림 전송 - 유저 ID : "+notification.getUserId()+" , 상품 ID : "+notification.getProductId());
    }
}
