



클라이언트 측 토큰화 (Client-Side Tokenization)

Browser          PaymentController    PaymentService    PaymentGatewayAdapter    PaymentPersistencePort
   |                    |                   |                    |                         |
   | POST /payments     |                   |                    |                         |
   |------------------>|                   |                    |                         |
   |                    |                   |                    |                         |
   |                    | processPayment()  |                    |                         |
   |                    |------------------>|                    |                         |
   |                    |                   | processPayment()   |                         |
   |                    |                   |------------------->|                         |
   |                    |                   |                    | processPayment()        |
   |                    |                   |                    |------------------------>|
   |                    |                   |                    |                         |
   |                    |                   |                    |       Payment           |
   |                    |                   |                    |<------------------------|
   |                    |                   |       Payment      |                         |
   |                    |                   |<-------------------|                         |
   |                    |       Payment     |                    |                         |
   |                    |<------------------|                    |                         |
   |       Payment      |                   |                    |                         |
   |<------------------|                   |                    |                         |


호스팅된 결제 페이지 (Hosted Payment Page)

Browser          PaymentController    PaymentService    PaymentGatewayAdapter    PaymentPersistencePort
   |                    |                   |                    |                         |
   | GET /payments/hpp  |                   |                    |                         |
   |------------------>|                   |                    |                         |
   |                    | buildFormDescriptor()                  |                         |
   |                    |------------------>|                    |                         |
   |                    |                   | buildFormDescriptor()                        |
   |                    |                   |------------------->|                         |
   |                    |                   |                    |                         |
   |                    |                   |    Form Descriptor |                         |
   |                    |                   |<-------------------|                         |
   |   Redirect URL     |                   |                    |                         |
   |<------------------|                   |                    |                         |
   |                    |                   |                    |                         |
   | User completes payment on gateway page |                    |                         |
   |----------------------------------------|                    |                         |
   |                    |                   |                    |                         |
   | POST /payments/notify                  |                    |                         |
   |------------------>|                   |                    |                         |
   |                    | processNotification()                  |                         |
   |                    |------------------>|                    |                         |
   |                    |                   | updatePaymentStatus()                        |
   |                    |                   |------------------->|                         |
   |                    |                   |                    | updatePayment()         |
   |                    |                   |                    |------------------------>|
   |                    |                   |                    |                         |
   |    Confirmation    |                   |                    |                         |
   |<------------------|                   |                    |                         |

서버 측 토큰화 (Server-Side Tokenization)

Browser          PaymentController    PaymentService    PaymentGatewayAdapter    PaymentPersistencePort
   |                    |                   |                    |                         |
   | POST /payments     |                   |                    |                         |
   | (with card details)|                   |                    |                         |
   |------------------>|                   |                    |                         |
   |                    | processPayment()  |                    |                         |
   |                    |------------------>|                    |                         |
   |                    |                   | processPayment()   |                         |
   |                    |                   |------------------->|                         |
   |                    |                   |                    | tokenize()              |
   |                    |                   |                    |------------------------>|
   |                    |                   |                    |                         |
   |                    |                   |                    |       Token             |
   |                    |                   |                    |<------------------------|
   |                    |                   |                    | processPayment()        |
   |                    |                   |                    |------------------------>|
   |                    |                   |                    |                         |
   |                    |                   |                    |       Payment           |
   |                    |                   |                    |<------------------------|
   |                    |                   |       Payment      |                         |
   |                    |                   |<-------------------|                         |
   |                    |       Payment     |                    |                         |
   |                    |<------------------|                    |                         |
   |       Payment      |                   |                    |                         |
   |<------------------|                   |                    |                         |

게이트웨이 통합 결제 (Gateway Integration Payment)

Browser          PaymentController    PaymentService    PaymentGatewayAdapter    PaymentPersistencePort
   |                    |                   |                    |                         |
   | POST /payments     |                   |                    |                         |
   |------------------>|                   |                    |                         |
   |                    | processPayment()  |                    |                         |
   |                    |------------------>|                    |                         |
   |                    |                   | processPayment()   |                         |
   |                    |                   |------------------->|                         |
   |                    |                   |                    | processPayment()        |
   |                    |                   |                    |------------------------>|
   |                    |                   |                    |                         |
   |                    |                   |                    |       Payment           |
   |                    |                   |                    |<------------------------|
   |                    |                   |       Payment      |                         |
   |                    |                   |<-------------------|                         |
   |                    |       Payment     |                    |                         |
   |                    |<------------------|                    |                         |
   |       Payment      |                   |                    |                         |
   |<------------------|                   |                    |                         |


PaymentController는 외부 요청을 받아 PaymentService로 전달합니다.
PaymentService는 비즈니스 로직을 처리하고 필요한 작업을 PaymentGatewayAdapter에 위임합니다.
PaymentGatewayAdapter는 적절한 결제 게이트웨이와 통신하여 결제를 처리합니다.
PaymentPersistencePort는 결제 정보를 저장하고 업데이트합니다.

