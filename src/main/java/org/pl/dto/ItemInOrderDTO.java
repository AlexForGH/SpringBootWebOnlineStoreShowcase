package org.pl.dto;

import org.pl.dao.Item;

public record ItemInOrderDTO(Item item, Integer quantity) {
}
