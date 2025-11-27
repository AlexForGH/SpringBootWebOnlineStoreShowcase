package org.pl.service;

import jakarta.persistence.EntityNotFoundException;
import org.pl.dao.Item;
import org.pl.dao.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.pl.utils.ItemsUtils.itemsCounts;

@Service
public class CartService {

    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public CartService(
            ItemService itemService,
            OrderService orderService,
            OrderItemService orderItemService
    ) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @Transactional(
            rollbackFor = {EntityNotFoundException.class, RuntimeException.class}
    )
    public Order createSaveOrders() {
        Order savedOrder = orderService.createOrder(getTotalItemsSum());
        orderItemService.saveOrder(savedOrder, itemsCounts);
        itemsCounts.clear();
        return savedOrder;
    }

    @Transactional(
            rollbackFor = {EntityNotFoundException.class, RuntimeException.class}
    )
    public Order createSaveOrder(Long itemId) {
        Order savedOrder = orderService.createOrder(
                itemService.getPriceById(itemId).multiply(BigDecimal.valueOf(itemsCounts.get(itemId))
                )
        );
        orderItemService.saveOrder(
                savedOrder,
                itemsCounts.entrySet().stream()
                        .filter(entry -> entry.getKey().equals(itemId))
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue
                                )
                        )
        );
        itemsCounts.remove(itemId);
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByItemsCounts() {
        return itemsCounts.keySet().stream()
                .map(id -> itemService.getItemById(id).orElseThrow()).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalItemsSum() {
        return itemsCounts.entrySet().stream()
                .map(entry -> itemService.getPriceById(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
