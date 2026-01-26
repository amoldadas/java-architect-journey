# ADR-001: Domain Model and Aggregate Boundaries

## Status
Accepted

## Context
We are building an Order Management platform where customers place orders
for products and complete payments via a simulated payment provider.

The system must enforce strong consistency for:
- Order totals
- Order state transitions
- Payment idempotency

We want to start as a modular monolith and retain the ability to split into
microservices later without major refactoring.

## Decision
We adopt a Domain-Driven Design (DDD) approach with clear aggregates.

### Aggregates
1. **Order (Aggregate Root)**
   - Owns OrderItems
   - Enforces order state transitions
   - Holds price-at-purchase snapshots
   - Calculates and owns order total

2. **Product (Aggregate Root)**
   - Managed independently by Admin
   - Price changes apply only to future orders
   - Referenced only when creating OrderItems

3. **PaymentAttempt (Aggregate Root)**
   - Linked to Order by orderId
   - Handles retries and idempotency
   - Represents interaction with external payment provider

### State Management
Order state transitions are explicitly modeled and validated inside
the Order aggregate.

### Modular Structure
The system will be implemented as a modular monolith with clear module
boundaries:
- catalog
- ordering
- payments

## Consequences
### Positive
- Clear business invariants
- Strong consistency guarantees
- Clean future microservice extraction paths
- Reduced early distributed complexity

### Negative
- Single deployment unit initially
- Requires discipline to maintain module boundaries

## Alternatives Considered
1. Immediate microservices split  
   - Rejected due to unnecessary early complexity

2. Transaction script approach  
   - Rejected due to poor scalability of business rules

## Notes
Microservice extraction will use patterns such as:
- Strangler Fig
- Branch by Abstraction
- Database-per-service migration
