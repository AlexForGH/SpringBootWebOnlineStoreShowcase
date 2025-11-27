package org.pl.controller;

import org.pl.dao.Order;
import org.pl.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.pl.controller.Actions.*;
import static org.pl.utils.ItemsUtils.itemsCounts;

@Controller
@RequestMapping()
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(cartAction)
    public String cartAction(Model model) {
        itemsCounts.entrySet().removeIf(entry -> entry.getValue() == 0);
        model.addAttribute("itemsCounts", itemsCounts);
        model.addAttribute("items", cartService.getItemsByItemsCounts());
        model.addAttribute("cartAction", cartAction);
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("buyAction", buyAction);
        model.addAttribute("totalItemsSum", cartService.getTotalItemsSum());
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
        Long orderId = 0L;
        try {
            Order savedOrder = cartService.createSaveOrders();
            orderId = savedOrder.getId();
            addFlashAttributeForBuyItems(redirectAttributes, savedOrder.getOrderNumber(), null);
        } catch (Exception e) {
            addFlashAttributeForBuyItems(redirectAttributes, null, e);
        }

        return "redirect:" + ordersAction + "/" + orderId;
    }

    @PostMapping(buyAction + "/{id}")
    public String buyItem(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id
    ) {
        Long orderId = 0L;
        try {
            Order savedOrder = cartService.createSaveOrder(id);
            orderId = savedOrder.getId();
            addFlashAttributeForBuyItems(redirectAttributes, savedOrder.getOrderNumber(), null);
        } catch (Exception e) {
            addFlashAttributeForBuyItems(redirectAttributes, null, e);
        }
        return "redirect:" + ordersAction + "/" + orderId;
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
