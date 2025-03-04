## **비즈니스 요구 사항**

- 재입고 알림을 전송하기 전, 상품의 재입고 회차를 1 증가 시킨다.

재입고된 상품의 재고 회차를 관리하기 위해 상품이 재입고 되었을 때, 해당 상품의 재입고 회차와 수량을 증가시켰습니다.


- 상품이 재입고 되었을 때, 재입고 알림을 설정한 유저들에게 알림 메시지를 전달해야 한다.
- 재입고 알림은 재입고 알림을 설정한 유저 순서대로 메시지를 전송한다.

상품이 재입고될 때 NotificationManager가 실행되며, ProductUserNotification 리스트를 만들어 알림을 받을 유저 목록을 생성했습니다. 해당 유저 목록을 바탕으로 순차적으로 알림을 전송합니다.
알림 전송은 consumer를 사용하여 BlockingQueue를 처리했습니다.

- 회차별 재입고 알림을 받은 유저 목록을 저장해야 한다.

알림을 전송 중일 때 알림 받은 유저들을 하나씩 history 테이블에 저장했습니다.

- 재입고 알림을 보내던 중 재고가 모두 없어진다면 알림 보내는 것을 중단합니다.

캐시에 저장된 알림 전송 상태가 품절로 변경되면 DB와 캐시에 각각 전송 상태를 품절로 저장하고, 알림 전송을 중단하도록 했습니다. 의사 결정을 보면서 확인해보니 재고 수량을 가져와 확인하는 방향으로 변경해야 할 듯 합니다. 추후 변경하도록 하겠습니다.

- 재입고 알림 전송의 상태를 DB 에 저장해야 한다.
    - IN_PROGRESS (발송 중)
    - CANCELED_BY_SOLD_OUT (품절에 의한 발송 중단)
    - CANCELED_BY_ERROR (예외에 의한 발송 중단)
    - COMPLETED (완료)


알림을 전송하기 전에 최초 DB에 알림 상태를 발송 중으로 저장했습니다.
caffeine 라이브러리를 사용해 캐시에 productId에 맞는 알림 전송 상태를 미리 저장해놓고, 알림 전송 중에 캐시에서 상태를 확인하여 재고 수량에 따라 전송 상태를 저장하도록 했습니다. 알림 전송이 완료되면 completed를 저장하고, 마지막 userId를 저장했습니다.


## 기술적 요구 사항

- 알림 메시지는 1초에 최대 500개의 요청을 보낼 수 있다.

알림 전송은 Bucket4j를 사용하여 1초에 500개의 알림만 전송할 수 있도록 토큰 버킷 알고리즘을 사용했습니다. consumer에서 알림을 처리할 때 버킷에 남아있는 토큰 수를 확인하며, 토큰이 부족할 경우에는 큐에 다시 넣어 처리 대기하도록 했습니다. 



## **Bucket, Cache, 생산자, 소비자 패턴을 사용한 알림 처리 기능**

<center><img src="https://github.com/user-attachments/assets/7f96bbb9-1e66-4c94-89da-8a65436c2739" width="500" height="300" /></center>

알림 처리는 위의 그림과 같은 구조로 로직이 처리됩니다.</br>
NotificationManager가 요청을 받아 producer에서 전달합니다.</br> producer는 받는 요청을 BlockingDeque에 저장합니다. </br>Comsumer가 BlockingDeque에서 알림 설정한 유저들을 하나씩 꺼내며 요청을 처리합니다. </br>
재입고 알림을 전송 하기 전에 발송 중을 DB와 캐시에 저장합니다. </br>consumer에서 재입고 알림 전송이 완료되면 DB와 캐시에 저장합니다.</br> consumer는 알림 전송을 진행하면서 캐시에서 알림 전송 상태를 가져와 확인합니다.</br> 상품이 sold out이 되면 알림을 중단하도록 했습니다.</br>



시간이 부족하여 상세한 테스트는 진행하지 못했습니다. 추후 변경하도록 하겠습니다.
