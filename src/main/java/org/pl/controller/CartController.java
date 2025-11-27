package org.pl.controller;

import org.pl.dao.Item;
import org.pl.dao.Order;
import org.pl.service.ItemService;
import org.pl.service.OrderItemService;
import org.pl.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.pl.controller.Actions.*;
import static org.pl.utils.ItemsUtils.itemsCounts;

@Controller
@RequestMapping()
public class CartController {

    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public CartController(ItemService itemService, OrderService orderService, OrderItemService orderItemService) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping(cartAction)
    public String cartAction(Model model) {
        itemsCounts.entrySet().removeIf(entry -> entry.getValue() == 0);

        List<Item> items = itemsCounts.keySet().stream()
                .map(id -> itemService.getItemById(id).orElseThrow()).toList();

        model.addAttribute("itemsCounts", itemsCounts);
        model.addAttribute("items", items);
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

    @PostMapping(buyAction)
    public String buyItems(RedirectAttributes redirectAttributes) {
        Long id = 0L;
        try {
            Order savedOrder = orderService.createOrder(getTotalItemsSum(itemsCounts));
            id = savedOrder.getId();
            orderItemService.saveOrder(savedOrder, itemsCounts);
            itemsCounts.clear();
            addFlashAttributeForBuyItems(redirectAttributes, savedOrder.getOrderNumber(), null);
        } catch (Exception e) {
            addFlashAttributeForBuyItems(redirectAttributes, null, e);
        }

        return "redirect:" + ordersAction + "/" + id;
    }

    @PostMapping(buyAction + "/{id}")
    public String buyItems(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id
    ) {
        try {
            Order savedOrder = orderService.createOrder(
                    itemService.getPriceById(id).multiply(BigDecimal.valueOf(itemsCounts.get(id))
                    )
            );
            orderItemService.saveOrder(
                    savedOrder,
                    itemsCounts.entrySet().stream()
                            .filter(entry -> entry.getKey().equals(id))
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue
                                    )
                            )
            );
            itemsCounts.remove(id);
            addFlashAttributeForBuyItems(redirectAttributes, savedOrder.getOrderNumber(), null);
        } catch (Exception e) {
            addFlashAttributeForBuyItems(redirectAttributes, null, e);
        }
        return "redirect:" + ordersAction + "/" + id;
    }

    private BigDecimal getTotalItemsSum(Map<Long, Integer> itemsCounts) {
        return itemsCounts.entrySet().stream()
                .map(entry -> itemService.getPriceById(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void addFlashAttributeForBuyItems(
            RedirectAttributes redirectAttributes,
            String orderNumber,
            Exception e
    ) {
        if (e == null) {
            redirectAttributes.addFlashAttribute(
                    "toastMessage",
                    "Заказ №" + orderNumber + " успешно оформлен!"
            );
            redirectAttributes.addFlashAttribute("toastType", "success");
        } else {
            redirectAttributes.addFlashAttribute(
                    "toastMessage",
                    "Ошибка при оформлении заказа: " + e.getMessage()
            );
            redirectAttributes.addFlashAttribute("toastType", "error");
        }
    }
}
