package org.pl.controller;

import org.pl.dao.Item;
import org.pl.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.pl.controller.Actions.*;

@Controller
@RequestMapping()
public class CartController {

    private final ItemService itemService;

    private Map<Long, Integer> itemsCounts = new HashMap<>();

    public CartController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(cartAction)
    public String cartAction(
            @ModelAttribute("itemsCounts") Map<Long, Integer> itemsCounts,
            Model model
    ) {
        this.itemsCounts = itemsCounts;

        itemsCounts.entrySet().removeIf(entry -> entry.getValue() == 0);

        List<Item> items = this.itemsCounts.keySet().stream()
                .map(id -> itemService.getItemById(id).orElseThrow()).toList();

        model.addAttribute("itemsCounts", itemsCounts);
        model.addAttribute("items", items);
        model.addAttribute("ordersAction", ordersAction);
        model.addAttribute("cartAction", cartAction);
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("buyAction", buyAction);
        model.addAttribute("totalItemsSum", getTotalItemsSum(itemsCounts));
        return "cart";
    }

    @PostMapping(cartAction)
    public String increaseDecreaseItemsCount(
            @RequestParam Long id,
            @RequestParam String action,
            RedirectAttributes redirectAttributes
    ) {
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
            case "DELETE":
                itemsCounts.remove(id);
                break;
        }
        redirectAttributes.addFlashAttribute("itemsCounts", itemsCounts);
        return "redirect:" + cartAction;
    }

    private BigDecimal getTotalItemsSum(Map<Long, Integer> itemsCounts) {
        return itemsCounts.entrySet().stream()
                .map(entry -> itemService.getPriceById(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
