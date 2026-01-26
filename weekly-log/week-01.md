
---

## 3️⃣ `weekly-log/week-01.md`

```md
# Week 01 – Domain Modeling & Project Setup

## Goals
- Define business scope
- Establish ubiquitous language
- Identify aggregates and boundaries
- Create repository and documentation structure

## Completed
- Business scope finalized
- Aggregates identified (Order, Product, PaymentAttempt)
- Order state machine defined
- Modular monolith approach chosen
- ADR-001 created
- C4 context diagram drafted
- Repository structure initialized

## Key Decisions
- Start as modular monolith, split later
- PaymentAttempt modeled as separate aggregate
- Order owns price-at-purchase snapshots

## Open Questions
- When to introduce inventory constraints
- How payment retries will evolve under load

## Next Week Preparation
- Introduce persistence layer
- Map aggregates to JPA entities
- Define transactional boundaries
