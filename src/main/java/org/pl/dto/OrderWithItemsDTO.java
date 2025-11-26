package org.pl.dto;

import org.pl.dao.Order;

import java.util.List;

public record OrderWithItemsDTO(Order order, List<ItemInOrderDTO> itemInOrderDTOs) {
}
