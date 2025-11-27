package org.pl.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.pl.dao.Item;
import org.pl.dao.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final SessionItemsCountsService sessionItemsCountsService;

    public CartService(
            ItemService itemService,
            OrderService orderService,
            OrderItemService orderItemService,
            SessionItemsCountsService sessionItemsCountsService
    ) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.sessionItemsCountsService = sessionItemsCountsService;
    }

    @Transactional(
            rollbackFor = {EntityNotFoundException.class, RuntimeException.class}
    )
    public Order createSaveOrders(HttpSession httpSession) {
        Order savedOrder = orderService.createOrder(getTotalItemsSum(httpSession));
        orderItemService.saveOrder(savedOrder, sessionItemsCountsService.getCartItems(httpSession));
        sessionItemsCountsService.clearCartItems(httpSession);
        return savedOrder;
    }

    @Transactional(
            rollbackFor = {EntityNotFoundException.class, RuntimeException.class}
    )
    public Order createSaveOrder(Long itemId, HttpSession httpSession) {
        Order savedOrder = orderService.createOrder(
                itemService.getPriceById(itemId).multiply(
                        BigDecimal.valueOf(
                                sessionItemsCountsService.getCartItems(httpSession).get(itemId)
                        )
                )
        );
        orderItemService.saveOrder(
                savedOrder,
                sessionItemsCountsService.getCartItems(httpSession).entrySet().stream()
                        .filter(entry -> entry.getKey().equals(itemId))
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue
                                )
                        )
        );
        sessionItemsCountsService.getCartItems(httpSession).remove(itemId);
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByItemsCounts(HttpSession httpSession) {
        return sessionItemsCountsService.getCartItems(httpSession).keySet().stream()
                .map(id -> itemService.getItemById(id).orElseThrow()).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalItemsSum(HttpSession httpSession) {
        return sessionItemsCountsService.getCartItems(httpSession).entrySet().stream()
                .map(entry -> itemService.getPriceById(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
