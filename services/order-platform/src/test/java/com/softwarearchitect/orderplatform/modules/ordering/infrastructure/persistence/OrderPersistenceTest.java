package com.softwarearchitect.orderplatform.modules.ordering.infrastructure.persistence;

import com.softwarearchitect.orderplatform.modules.ordering.domain.Order;
import com.softwarearchitect.orderplatform.modules.ordering.domain.OrderItem;
import com.softwarearchitect.orderplatform.shared.domain.Money;
import com.softwarearchitect.orderplatform.shared.domain.ids.OrderId;
import com.softwarearchitect.orderplatform.shared.domain.ids.ProductId;
import com.softwarearchitect.orderplatform.testsupport.FlywayTestContextInitializer;
import com.softwarearchitect.orderplatform.testsupport.PostgresContainers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;


import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = FlywayTestContextInitializer.class)
class OrderPersistenceTest extends PostgresContainers {

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        r.add("spring.datasource.username", POSTGRES::getUsername);
        r.add("spring.datasource.password", POSTGRES::getPassword);

        r.add("spring.flyway.enabled", () -> "false");
        r.add("spring.jpa.hibernate.ddl-auto", () -> "validate");

        // safest timezone fix (no global JVM change)
        r.add("spring.jpa.properties.hibernate.jdbc.time_zone", () -> "UTC");
        //r.add("spring.datasource.hikari.connection-init-sql", () -> "SET TIME ZONE 'UTC'");
    }

    @Autowired
    private OrderJpaRepository repo;

    @Test
    void persists_order_and_items() {
        Order order = Order.createNew(OrderId.newId(), "INR");
        order.addItem(new OrderItem(ProductId.newId(), "Book",
                Money.of(new BigDecimal("100.00"), "INR"), 2));
        order.markPaymentPending();

        OrderEntity entity = new OrderPersistenceMapper().toEntity(order);
        repo.save(entity);

        OrderEntity saved = repo.findById(entity.getId()).orElseThrow();
        assertThat(saved.getItems()).hasSize(1);
        assertThat(saved.getStatus()).isEqualTo("PAYMENT_PENDING");
    }
}