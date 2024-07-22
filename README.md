
```mermaid
graph TB
    subgraph "Domain Layer"
        Payment
        PaymentMethod
        PaymentStatus
    end

    subgraph "Application Layer"
        PaymentUseCase
        PaymentService
        PaymentGatewayPort
        PaymentPersistencePort
    end

    subgraph "Infrastructure Layer"
        subgraph "Web"
            PaymentController
        end
        subgraph "Persistence"
            PaymentEntity
            PaymentMethodEntity
            PaymentRepository
        end
        subgraph "Gateway"
            PaymentGatewayAdapter
            PayPalAdapter
            StripeAdapter
        end
    end

    %% Domain Layer relationships
    Payment --> PaymentMethod
    Payment --> PaymentStatus

    %% Application Layer relationships
    PaymentService --> PaymentUseCase
    PaymentService --> PaymentGatewayPort
    PaymentService --> PaymentPersistencePort

    %% Infrastructure Layer relationships
    PaymentController --> PaymentUseCase
    PaymentRepository --> PaymentPersistencePort
    PaymentGatewayAdapter --> PaymentGatewayPort
    PayPalAdapter --> PaymentGatewayAdapter
    StripeAdapter --> PaymentGatewayAdapter

    %% Cross-layer relationships
    PaymentController -.-> Payment
    PaymentEntity -.-> Payment
    PaymentMethodEntity -.-> PaymentMethod
    PaymentGatewayAdapter -.-> Payment
```
도메인 레이어: 
Payment, PaymentMethod, PaymentStatus 등 핵심 도메인 모델

애플리케이션 레이어:
PaymentUseCase: 결제 관련 유스케이스를 정의하는 인터페이스
PaymentService: 실제 비즈니스 로직을 구현하는 서비스
PaymentGatewayPort: 결제 게이트웨이와의 통신을 추상화하는 포트
PaymentPersistencePort: 결제 정보 영속성을 추상화하는 포트

인프라스트럭처 레이어:
Web: PaymentController가 외부 요청을 처리합니다.
Persistence: PaymentEntity, PaymentMethodEntity, PaymentRepository가 데이터 영속성
Gateway: PaymentGatewayAdapter, PayPalAdapter, StripeAdapter가 실제 결제 게이트웨이와의 인터페이스 구현


레이어 간 관계:
애플리케이션 레이어는 도메인 모델을 사용합니다.
인프라스트럭처 레이어는 애플리케이션 레이어의 인터페이스(포트)를 구현합니다.
의존성 방향이 항상 내부(도메인)를 향하고 있어야 하며 이는 헥사고날 아키텍처의 원칙에 따라 디자인되었음



클라이언트 측 토큰화 (Client-Side Tokenization)
```mermaid
sequenceDiagram
    participant Browser
    participant MerchantSite
    participant PaymentController
    participant PaymentService
    participant PaymentGatewayAdapter
    participant PaymentPersistencePort

    Browser->>MerchantSite: 결제 정보 입력
    MerchantSite->>MerchantSite: 토큰 생성
    MerchantSite->>PaymentController: addPaymentMethod(token)
    PaymentController->>PaymentService: processPayment(Payment)
    PaymentService->>PaymentGatewayAdapter: processPayment(Payment)
    PaymentGatewayAdapter->>PaymentGatewayAdapter: 게이트웨이 선택
    PaymentGatewayAdapter-->>PaymentService: 처리된 결제
    PaymentService->>PaymentPersistencePort: savePayment(Payment)
    PaymentPersistencePort-->>PaymentService: 저장된 결제
    PaymentService-->>PaymentController: 결제 결과
    PaymentController-->>MerchantSite: 결제 결과
    MerchantSite-->>Browser: 결제 완료 확인
```

호스팅된 결제 페이지 (Hosted Payment Page)
```mermaid
sequenceDiagram
    participant Browser
    participant MerchantSite
    participant PaymentController
    participant PaymentService
    participant PaymentGatewayAdapter
    participant PaymentGateway
    participant PaymentPersistencePort

    Browser->>MerchantSite: 체크아웃 시작
    MerchantSite->>PaymentController: buildFormDescriptor()
    PaymentController->>PaymentService: prepareHostedPayment()
    PaymentService->>PaymentGatewayAdapter: getRedirectUrl()
    PaymentGatewayAdapter->>PaymentGateway: 리다이렉트 URL 요청
    PaymentGateway-->>PaymentGatewayAdapter: 리다이렉트 URL
    PaymentGatewayAdapter-->>PaymentService: 리다이렉트 URL
    PaymentService-->>PaymentController: 리다이렉트 URL
    PaymentController-->>MerchantSite: 리다이렉트 URL
    MerchantSite->>Browser: 리다이렉트
    Browser->>PaymentGateway: 결제 정보 입력
    PaymentGateway->>PaymentGateway: 결제 처리
    PaymentGateway->>Browser: 결제 완료, 리다이렉트
    Browser->>MerchantSite: 리다이렉트 (결제 정보 포함)
    MerchantSite->>PaymentController: completeHostedPayment()
    PaymentController->>PaymentService: processPayment(Payment)
    PaymentService->>PaymentGatewayAdapter: processPayment(Payment)
    PaymentGatewayAdapter->>PaymentGateway: 결제 확인
    PaymentGateway-->>PaymentGatewayAdapter: 결제 상태
    PaymentGatewayAdapter-->>PaymentService: 처리된 결제
    PaymentService->>PaymentPersistencePort: savePayment(Payment)
    PaymentPersistencePort-->>PaymentService: 저장된 결제
    PaymentService-->>PaymentController: 결제 결과
    PaymentController-->>MerchantSite: 결제 결과
    MerchantSite-->>Browser: 결제 완료 확인
```

서버 측 토큰화 (Server-Side Tokenization)
```mermaid
sequenceDiagram
    participant Browser
    participant MerchantSite
    participant PaymentController
    participant PaymentService
    participant PaymentGatewayAdapter
    participant PaymentGateway
    participant PaymentPersistencePort

    Browser->>MerchantSite: 결제 정보 입력
    MerchantSite->>PaymentController: addPaymentMethod(cardDetails)
    PaymentController->>PaymentService: processPayment(Payment)
    PaymentService->>PaymentGatewayAdapter: processPayment(Payment)
    PaymentGatewayAdapter->>PaymentGateway: 토큰화 요청
    PaymentGateway-->>PaymentGatewayAdapter: 토큰
    PaymentGatewayAdapter->>PaymentGateway: 결제 처리
    PaymentGateway-->>PaymentGatewayAdapter: 결제 상태
    PaymentGatewayAdapter-->>PaymentService: 처리된 결제
    PaymentService->>PaymentPersistencePort: savePayment(Payment)
    PaymentPersistencePort-->>PaymentService: 저장된 결제
    PaymentService-->>PaymentController: 결제 결과
    PaymentController-->>MerchantSite: 결제 결과
    MerchantSite-->>Browser: 결제 완료 확인
```
게이트웨이 통합 흐름 (Gateway Integration Flow)
```mermaid
sequenceDiagram
    participant Browser
    participant MerchantSite
    participant PaymentController
    participant PaymentService
    participant PaymentGatewayAdapter
    participant SpecificGatewayAdapter
    participant PaymentGateway
    participant PaymentPersistencePort

    Browser->>MerchantSite: 결제 요청
    MerchantSite->>PaymentController: processPayment(Payment)
    PaymentController->>PaymentService: processPayment(Payment)
    PaymentService->>PaymentGatewayAdapter: processPayment(Payment)
    PaymentGatewayAdapter->>PaymentGatewayAdapter: 게이트웨이 선택
    PaymentGatewayAdapter->>SpecificGatewayAdapter: processPayment(Payment)
    SpecificGatewayAdapter->>PaymentGateway: 결제 요청
    PaymentGateway-->>SpecificGatewayAdapter: 결제 결과
    SpecificGatewayAdapter-->>PaymentGatewayAdapter: 처리된 결제
    PaymentGatewayAdapter-->>PaymentService: 처리된 결제
    PaymentService->>PaymentPersistencePort: savePayment(Payment)
    PaymentPersistencePort-->>PaymentService: 저장된 결제
    PaymentService-->>PaymentController: 결제 결과
    PaymentController-->>MerchantSite: 결제 결과
    MerchantSite-->>Browser: 결제 완료 확인
```
