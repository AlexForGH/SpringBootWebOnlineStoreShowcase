package org.pl.controller;

import org.pl.dao.Item;
import org.pl.dto.PagingInfoDto;
import org.pl.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.pl.controller.Actions.*;
import static org.pl.utils.ItemsUtils.*;


@Controller
@RequestMapping()
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping()
    public String redirectToItems() {
        return "redirect:" + itemsAction;
    }

    @GetMapping(itemsAction)
    public String getItemsSorted(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "NO") String sort,
            @RequestParam(required = false) String search, Model model
    ) {
        checkItemsCount();

        // Учитываем, что пользователь видит страницы с 1, а Spring Data с 0
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<List<Item>> itemPage = itemService.getItemsSorted(pageable, sort, search);

        model.addAttribute("items", itemPage.getContent()); // Список списков!
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("itemsCounts", itemsCounts); model.addAttribute("totalItemsCounts", totalItemsCounts);
        model.addAttribute("paging", new PagingInfoDto(itemPage.getNumber() + 1, itemPage.getTotalPages(), itemPage.getSize(), itemPage.hasPrevious(), itemPage.hasNext()));
        model.addAttribute("ordersAction", ordersAction);
        model.addAttribute("cartAction", cartAction);
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("itemsToCartAction", itemsToCartAction);
        return "items";
    }

    @PostMapping(itemsAction)
    public String increaseDecreaseItemsCount(@RequestParam Long id, @RequestParam String action, @RequestParam String search, @RequestParam int pageNumber) {
        increaseDecreaseCount(action, id);
        return "redirect:" + itemsAction + "?pageNumber=" + pageNumber + "&search=" + search;
    }

    @GetMapping(itemsToCartAction)
    public String redirectToItemsToCart(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("itemsCounts", itemsCounts);
        return "redirect:" + cartAction;
    }

    @GetMapping(itemsAction + "/{id}")
    public String getItemById(@PathVariable Long id, Model model) {
        checkItemsCount();

        Item item = itemService.getItemById(id).orElseThrow();
        model.addAttribute("item", item);
        model.addAttribute("ordersAction", ordersAction);
        model.addAttribute("cartAction", cartAction);
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("itemCounts", itemsCounts.getOrDefault(id, 0));
        model.addAttribute("itemsToCartAction", itemsToCartAction);
        model.addAttribute("totalItemsCounts", totalItemsCounts);
        model.addAttribute("buyAction", buyAction);
        return "item";
    }

    @PostMapping(itemsAction + "/{id}")
    public String increaseDecreaseItemCount(@PathVariable Long id, @RequestParam String action) {
        increaseDecreaseCount(action, id);
        return "redirect:" + itemsAction + "/" + id;
    }

    private void increaseDecreaseCount(String action, Long id) {
        int currentCount = itemsCounts.getOrDefault(id, 0);

        switch (action) {
            case "PLUS":
                itemsCounts.put(id, currentCount + 1);
                break;
            case "MINUS":
                if (currentCount > 0) {
                    itemsCounts.put(id, currentCount - 1);
                }
                break;
        }
    }
}
