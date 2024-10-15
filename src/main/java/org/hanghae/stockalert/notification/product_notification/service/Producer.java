package org.hanghae.stockalert.notification.product_notification.service;

import org.hanghae.stockalert.notification.product_user_notification.entity.ProductUserNotification;



import java.util.concurrent.BlockingDeque;



public class Producer{

    private final BlockingDeque<ProductUserNotification> queue;

    public Producer(BlockingDeque<ProductUserNotification> queue) {
        this.queue = queue;
    }

    public void produce(ProductUserNotification notification) {
        queue.offer(notification);
    }
}
