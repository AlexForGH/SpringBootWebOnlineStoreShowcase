package org.pl.repository;

import org.pl.dao.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order JOIN FETCH oi.item")
    List<OrderItem> findAllWithAssociations();

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order JOIN FETCH oi.item WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdWithAssociations(@Param("orderId") Long orderId);
}
