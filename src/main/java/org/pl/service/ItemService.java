package org.pl.service;

import org.pl.dao.Item;
import org.pl.repository.ItemRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<List<Item>> getItemsSorted(Pageable pageable, String sortBy, String title) {

        Page<Item> itemPage;
        if (title == null || title.isEmpty()) {
            itemPage = switch (sortBy) {
                case "PRICE_ASC" -> itemRepository.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by("price").ascending()
                        )
                );
                case "PRICE_DESC" -> itemRepository.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by("price").descending()
                        )
                );
                case "ALPHA_ASC" -> itemRepository.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Order.asc("title").ignoreCase())
                        )
                );
                case "ALPHA_DESC" -> itemRepository.findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Order.desc("title").ignoreCase())
                        )
                );
                case "NO" -> itemRepository.findAll(pageable);
                default -> throw new IllegalStateException("Unexpected value: " + sortBy);
            };
        } else {
            itemPage = switch (sortBy) {
                case "PRICE_ASC" -> itemRepository.findByTitleContainingIgnoreCase(
                        title,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by("price").ascending()
                        )
                );
                case "PRICE_DESC" -> itemRepository.findByTitleContainingIgnoreCase(
                        title,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by("price").descending()
                        )
                );
                case "ALPHA_ASC" -> itemRepository.findByTitleContainingIgnoreCase(
                        title,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Order.asc("title").ignoreCase())
                        )
                );
                case "ALPHA_DESC" -> itemRepository.findByTitleContainingIgnoreCase(
                        title,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Order.desc("title").ignoreCase())
                        )
                );
                case "NO" -> itemRepository.findByTitleContainingIgnoreCase(title, pageable);
                default -> throw new IllegalStateException("Unexpected value: " + sortBy);
            };
        }

        // Создаем новую Page с нашим списком списков
        return new PageImpl<>(
                chunkList(itemPage.getContent()),
                pageable,
                itemPage.getTotalElements()
        );
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public BigDecimal getPriceById(Long id) {
        return getItemById(id).orElseThrow().getPrice();
    }

    private List<List<Item>> chunkList(List<Item> items) {
        int chunkSize = 3;
        return IntStream.range(0, (items.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> items.subList(
                        i * chunkSize,
                        Math.min(items.size(), (i + 1) * chunkSize)
                ))
                .collect(Collectors.toList());
    }
}
