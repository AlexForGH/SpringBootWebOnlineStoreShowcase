package org.pl.repository;

import org.pl.dao.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.orderNumber FROM Order o ORDER BY o.orderDate DESC LIMIT 1")
    String findLastOrderNumber();
}
