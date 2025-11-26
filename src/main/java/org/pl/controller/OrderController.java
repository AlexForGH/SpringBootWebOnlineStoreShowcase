package org.pl.controller;

import org.pl.service.OrderItemService;
import org.pl.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.pl.controller.Actions.*;

@Controller
@RequestMapping()
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping(ordersAction)
    public String getOrders(Model model) {
        model.addAttribute("ordersWithItems", orderItemService.getOrdersWithItems());
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("ordersAction", ordersAction);
        return "orders";
    }

    @GetMapping(ordersAction + "/{id}")
    public String getOrderById(@PathVariable Long id, Model model) {
        model.addAttribute("orderWithItems", orderItemService.getOrderWithItems(id));
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("ordersAction", ordersAction);
        return "order";
    }

}
