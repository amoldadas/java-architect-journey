# Module Boundaries (Modular Monolith Rules)

## Purpose
Prevent accidental coupling between modules so that future service extraction remains easy.

## Modules
- catalog
- ordering
- payments
- shared (only truly generic value objects)

## Allowed dependencies (Week 1)
- modules/*/domain may depend on shared/domain only
- No domain package may import another module’s domain package

## Future (Week 2+)
- Cross-module calls must go via application layer interfaces ("ports")
- Infrastructure (DB, messaging) must not leak into domain

## Naming rules
- Domain objects: nouns (Order, Product, PaymentAttempt)
- Domain methods: business verbs (markPaid, cancel)
- No “Service” in domain packages
