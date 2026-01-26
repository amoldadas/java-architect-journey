# C4 – System Context Diagram

```mermaid
flowchart LR
    Customer[Customer]
    Admin[Admin]
    OrderPlatform[Order Platform]
    PaymentProvider[External Payment Provider (Simulated)]

    Customer -->|Place Order / View Order| OrderPlatform
    Admin -->|Manage Products / View Orders| OrderPlatform
    OrderPlatform -->|Payment Request| PaymentProvider
    PaymentProvider -->|Payment Result| OrderPlatform
