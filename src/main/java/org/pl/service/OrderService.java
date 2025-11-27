package org.pl.service;

import org.pl.dao.Order;
import org.pl.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional()
    public Order createOrder(BigDecimal totalAmount) {
        Order newOrder = new Order(
                generateNextOrderNumber(),
                totalAmount,
                LocalDateTime.now()
        );
        return orderRepository.save(newOrder);
    }

    @Transactional(readOnly = true)
    public String generateNextOrderNumber() {
        int currentYear = LocalDate.now().getYear();

        // Получаем последний номер за текущий год
        String lastNumber = orderRepository.findLastOrderNumber();

        int nextSequence = 1;
        if (lastNumber != null) {
            // Разбираем счётчик из формата ORD-YYYY-NNN
            String[] parts = lastNumber.split("-");
            if (parts.length == 3) {
                int lastSequence = Integer.parseInt(parts[2]);
                nextSequence = lastSequence + 1;
            }
        }

        // Формируем новый номер: ORD-2025-001, ORD-2025-002 и т.д.
        return String.format("ORD-%d-%03d", currentYear, nextSequence);
    }
}
