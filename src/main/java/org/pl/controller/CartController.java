package org.pl.controller;

import jakarta.servlet.http.HttpSession;
import org.pl.dao.Order;
import org.pl.service.CartService;
import org.pl.service.SessionItemsCountsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.pl.controller.Actions.*;

@Controller
@RequestMapping()
public class CartController {

    private final CartService cartService;
    private final SessionItemsCountsService sessionItemsCountsService;

    public CartController(
            CartService cartService,
            SessionItemsCountsService sessionItemsCountsService
    ) {
        this.cartService = cartService;
        this.sessionItemsCountsService = sessionItemsCountsService;
    }

    @GetMapping(cartAction)
    public String cartAction(
            Model model,
            HttpSession httpSession
    ) {
        sessionItemsCountsService.getCartItems(httpSession)
                .entrySet()
                .removeIf(entry -> entry.getValue() == 0);
        model.addAttribute("cartItems", sessionItemsCountsService.getCartItems(httpSession));
        model.addAttribute("items", cartService.getItemsByItemsCounts(httpSession));
        model.addAttribute("cartAction", cartAction);
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("buyAction", buyAction);
        model.addAttribute("totalItemsSum", cartService.getTotalItemsSum(httpSession));
        return "cart";
    }

    @PostMapping(cartAction)
    public String increaseDecreaseItemsCount(
            @RequestParam Long id,
            @RequestParam String action,
            RedirectAttributes redirectAttributes,
            HttpSession httpSession
    ) {
        sessionItemsCountsService.updateItemCount(httpSession, id, action);
        redirectAttributes.addFlashAttribute(
                "cartItems",
                sessionItemsCountsService.getCartItems(httpSession)
        );
        return "redirect:" + cartAction;
    }

    @PostMapping(buyAction)
    public String buyItems(
            RedirectAttributes redirectAttributes,
            HttpSession httpSession
    ) {
        Long orderId = 0L;
        try {
            Order savedOrder = cartService.createSaveOrders(httpSession);
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
            @PathVariable Long id,
            HttpSession httpSession
    ) {
        Long orderId = 0L;
        try {
            Order savedOrder = cartService.createSaveOrder(id, httpSession);
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
