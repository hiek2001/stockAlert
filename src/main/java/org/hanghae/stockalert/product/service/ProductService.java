package org.hanghae.stockalert.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hanghae.stockalert.notification.product_notification.controller.NotificationManager;
import org.hanghae.stockalert.notification.product_notification.dto.NotificationRequestDto;
import org.hanghae.stockalert.notification.product_notification.entity.NotificationStatus;
import org.hanghae.stockalert.notification.product_notification.service.CacheService;
import org.hanghae.stockalert.notification.product_notification.service.ProductNotificationHistoryService;
import org.hanghae.stockalert.notification.product_user_notification.service.ProductUserNotificationHistoryService;
import org.hanghae.stockalert.notification.product_user_notification.service.UserNotificationService;
import org.hanghae.stockalert.product.entity.Product;
import org.hanghae.stockalert.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final UserNotificationService userNotificationService;
    private final ProductNotificationHistoryService notificationHistoryService;
    private final ProductUserNotificationHistoryService userNotificationHistoryService;
    private final CacheService cacheService;

    private final NotificationManager notificationManager;

    public void restockProduct(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("Product not found"));

        // 상품 재고 횟수 및 수량 증가
        product.reStock(quantity);

        // 상품별 재입고 알림 히스토리 저장
        notificationHistoryService.saveProductHistory(productId, product.getRestock_rounds(), NotificationStatus.IN_PROGRESS);

        NotificationStatus status = cacheService.getNotificationStatus(productId); // 캐시에서 알림 전송 상태 가져오기
        if(status == null) { // 캐시에 없으면
            status = notificationHistoryService.findStatusByProductId(productId);
            cacheService.saveNotificationStatus(productId, status); // 캐시에 저장
        }

        // 재입고 신청한 유저 목록 가져오기
        //List<ProductUserNotification> userNotifications = userNotificationService.getUserNotifications(productId);
        List<Long> userIdNotifications = userNotificationService.getUserNotifications(productId);

        NotificationRequestDto requestDto = new NotificationRequestDto(productId, userIdNotifications, status);
        notificationManager.notifyRestock(requestDto);

        // 상품 재입고 히스토리의 알람 상태 업데이트하기 위해 가져온 것
//        ProductNotificationHistory productNotificationHistory = notificationHistoryService.getProductNotificationHistory(productId);
//        for (ProductUserNotification userNotification : userNotifications) { // 유저 목록을 토대로 알림 전송
//            if(product.isSoldOut()) {
//                productNotificationHistory.updateSoldOutHistory(NotificationStatus.CANCELED_BY_SOLD_OUT, userNotification.getUserId());
//                break;
//            }
//
//            // 알림 전송 중 로직 추가
//
//            // 알림 전송 중, 유저별 알림 히스토리 저장
//            userNotificationHistoryService.saveUserNotificationHistory(userNotification.getUserId(), userNotification.getProductId(),product.getRestock_rounds());
//        }
//
//        // 알림 전송 완료
//        productNotificationHistory.completedNotification(NotificationStatus.COMPLETED);
    }

    @Transactional
    public void sellProduct(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("Product not found"));

        product.sell(quantity);
    }

    public int getProductRestockRounds(Long productId) {
        return productRepository.findRestockRoundsById(productId);
    }
}
