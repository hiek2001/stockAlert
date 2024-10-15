package org.hanghae.stockalert.notification.product_notification.controller;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.hanghae.stockalert.notification.product_notification.dto.NotificationRequestDto;
import org.hanghae.stockalert.notification.product_notification.service.Consumer;
import org.hanghae.stockalert.notification.product_notification.service.Producer;
import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class NotificationManager {

    private final Consumer consumer;
    private final Producer producer;


    @PostConstruct
    public void init() {
        new Thread(consumer).start(); // consumer 실행
    }

    public void notifyRestock(NotificationRequestDto requestDto) {
        toModel(requestDto).forEach(producer::produce); // producer를 통해 consumer로 전달
    }

    public List<ProductUserNotification> toModel(NotificationRequestDto requestDto) {
        List<ProductUserNotification> models = requestDto.getUsers().stream()
                .map(userId -> new ProductUserNotification(requestDto.getProductId(), userId))
                .collect(Collectors.toList());

        if (!models.isEmpty()) {
            ProductUserNotification lastElement = models.get(models.size() - 1);
            lastElement.endElement(); // 마지막 요소에 endElement() 호출, isLast가 true로 설정됨
        }

        return models;
    }
}
