package org.hanghae.stockalert.notification.product_notification.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import org.hanghae.stockalert.notification.product_notification.controller.NotificationManager;
import org.hanghae.stockalert.notification.product_notification.entity.ProductNotificationHistory;
import org.hanghae.stockalert.notification.product_notification.repository.ProductNotificationHistoryRepository;
import org.hanghae.stockalert.notification.product_notification.service.CacheService;
import org.hanghae.stockalert.notification.product_notification.service.Consumer;
import org.hanghae.stockalert.notification.product_notification.service.Producer;
import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotification;
import org.hanghae.stockalert.notification.product_user_notification.service.ProductUserNotificationHistoryService;
import org.hanghae.stockalert.product.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
@RequiredArgsConstructor
public class NotificationManagerConfig {

    private static final int REQUESTS_PER_SECOND = 500;

    private final ProductUserNotificationHistoryService userNotificationHistoryService;
    private final CacheService cacheService;
    private final ProductService productService;
    private final ProductNotificationHistoryRepository productHistoryRepository;

    @Bean
    public NotificationManager notificationManager() {
        return new NotificationManager(consumer(), producer());
    }

    @Bean
    public Consumer consumer() {
        return new Consumer(notiDeque(), notificationBucket(), userNotificationHistoryService, cacheService, productHistoryRepository, productService);
    }

    @Bean
    public Producer producer() {
        return new Producer(notiDeque());
    }

    @Bean
    public Bucket notificationBucket() {
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_SECOND, Refill.intervally(REQUESTS_PER_SECOND, Duration.ofSeconds(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Bean
    public BlockingDeque<ProductUserNotification> notiDeque() {
        return new LinkedBlockingDeque<>();
    }
}
