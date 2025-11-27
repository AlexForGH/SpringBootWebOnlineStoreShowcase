package org.pl.service;

import jakarta.persistence.EntityNotFoundException;
import org.pl.dao.Order;
import org.pl.dao.OrderItem;
import org.pl.dto.ItemInOrderDTO;
import org.pl.dto.OrderWithItemsDTO;
import org.pl.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    private final ItemService itemService;

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(
            ItemService itemService,
            OrderItemRepository orderItemRepository
    ) {
        this.itemService = itemService;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderWithItemsDTO> getOrdersWithItems() {
        // 1. Загружаем все OrderItem + связанные Order и Item одним запросом
        List<OrderItem> orderItems = orderItemRepository.findAllWithAssociations();

        // 2. Группируем OrderItem по заказу (order.id)
        Map<Long, List<OrderItem>> itemsByOrder = orderItems.stream()
                .collect(Collectors.groupingBy(
                        orderItem -> orderItem.getOrder().getId()
                ));

        // 3. Превращаем каждую группу в OrderWithItemsDTO
        return itemsByOrder.values().stream()
                .map(items -> {
                    // Берём первый OrderItem — его order уже загружен через JOIN FETCH
                    Order order = items.getFirst().getOrder();
                    // Формируем список ItemInOrderDTO
                    List<ItemInOrderDTO> itemDTOs = items.stream()
                            .map(item -> new ItemInOrderDTO(item.getItem(), item.getQuantity()))
                            .toList();
                    return new OrderWithItemsDTO(order, itemDTOs);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderWithItemsDTO getOrderWithItems(Long orderId) {
        // 1. Получаем все OrderItem для заказа с загруженными order и item
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithAssociations(orderId);
        if (orderItems.isEmpty()) {
            throw new EntityNotFoundException("Заказ с ID " + orderId + " не найден");
        }
        // 2. Берём первый элемент — его order уже загружен через JOIN FETCH
        Order order = orderItems.getFirst().getOrder();
        // 3. Формируем список ItemInOrderDTO
        List<ItemInOrderDTO> itemDTOs = orderItems.stream()
                .map(item -> new ItemInOrderDTO(item.getItem(), item.getQuantity()))
                .toList();
        // 4. Создаём итоговый DTO
        return new OrderWithItemsDTO(order, itemDTOs);
    }

    @Transactional()
    public void saveOrder(Order order, Map<Long, Integer> itemsCounts) {
        itemsCounts.forEach((itemId, quantity) -> {
            orderItemRepository.save(
                    new OrderItem(
                            order,
                            itemService.getItemById(itemId).orElseThrow(),
                            quantity
                    )
            );
        });
    }
}
