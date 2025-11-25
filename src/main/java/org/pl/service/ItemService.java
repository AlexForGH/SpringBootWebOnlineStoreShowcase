package org.pl.service;

import org.pl.dao.Item;
import org.pl.repository.ItemRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<List<Item>> getItemsSorted(Pageable pageable, String sortBy, int chunkSize) {
        // Получаем обычную страницу товаров
        Page<Item> itemPage = switch (sortBy) {
            case "PRICE_ASC" -> itemRepository.findAll(PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("price").ascending()
            ));
            case "PRICE_DESC" -> itemRepository.findAll(PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("price").descending()
            ));
            case "ALPHA_ASC" -> itemRepository.findAll(PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Order.asc("title").ignoreCase())
            ));
            case "ALPHA_DESC" -> itemRepository.findAll(PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Order.desc("title").ignoreCase())
            ));
            case "NO" -> itemRepository.findAll(pageable);
            default -> throw new IllegalStateException("Unexpected value: " + sortBy);
        };

        // Разбиваем на список списков (строки по chunkSize элементов)
        List<Item> items = itemPage.getContent();
        List<List<Item>> itemsInRows = IntStream.range(0, (items.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> items.subList(
                        i * chunkSize,
                        Math.min(items.size(), (i + 1) * chunkSize)
                ))
                .collect(Collectors.toList());

        // Создаем новую Page с нашим списком списков
        return new PageImpl<>(
                itemsInRows,
                pageable,
                itemPage.getTotalElements()
        );
    }
}
