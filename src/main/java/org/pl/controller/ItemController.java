package org.pl.controller;

import jakarta.servlet.http.HttpSession;
import org.pl.dao.Item;
import org.pl.dto.PagingInfoDto;
import org.pl.service.ItemService;
import org.pl.service.SessionItemsCountsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.pl.controller.Actions.*;


@Controller
@RequestMapping()
public class ItemController {

    private final ItemService itemService;
    private final SessionItemsCountsService sessionItemsCountsService;

    public ItemController(
            ItemService itemService,
            SessionItemsCountsService sessionItemsCountsService
    ) {
        this.itemService = itemService;
        this.sessionItemsCountsService = sessionItemsCountsService;
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
            @RequestParam(required = false) String search,
            Model model,
            HttpSession httpSession
    ) {
        sessionItemsCountsService.checkItemsCount(httpSession);

        // Учитываем, что пользователь видит страницы с 1, а Spring Data с 0
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<List<Item>> itemPage = itemService.getItemsSorted(pageable, sort, search);

        model.addAttribute("items", itemPage.getContent()); // Список списков!
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("cartItems", sessionItemsCountsService.getCartItems(httpSession));
        model.addAttribute("totalItemsCounts", sessionItemsCountsService.checkItemsCount(httpSession));
        model.addAttribute("paging", new PagingInfoDto(itemPage.getNumber() + 1, itemPage.getTotalPages(), itemPage.getSize(), itemPage.hasPrevious(), itemPage.hasNext()));
        model.addAttribute("ordersAction", ordersAction);
        model.addAttribute("cartAction", cartAction);
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("itemsToCartAction", itemsToCartAction);
        return "items";
    }

    @PostMapping(itemsAction)
    public String increaseDecreaseItemsCount(
            @RequestParam Long id,
            @RequestParam String action,
            @RequestParam String search,
            @RequestParam int pageNumber,
            HttpSession httpSession
    ) {
        sessionItemsCountsService.updateItemCount(httpSession, id, action);
        return "redirect:" + itemsAction + "?pageNumber=" + pageNumber + "&search=" + search;
    }

    @GetMapping(itemsToCartAction)
    public String redirectToItemsToCart(
            RedirectAttributes redirectAttributes,
            HttpSession httpSession
    ) {
        redirectAttributes.addFlashAttribute(
                "cartItems",
                sessionItemsCountsService.getCartItems(httpSession)
        );
        return "redirect:" + cartAction;
    }

    @GetMapping(itemsAction + "/{id}")
    public String getItemById(
            @PathVariable Long id,
            Model model,
            HttpSession httpSession) {
        sessionItemsCountsService.checkItemsCount(httpSession);

        Item item = itemService.getItemById(id).orElseThrow();
        model.addAttribute("item", item);
        model.addAttribute("ordersAction", ordersAction);
        model.addAttribute("cartAction", cartAction);
        model.addAttribute("itemsAction", itemsAction);
        model.addAttribute("itemCounts", sessionItemsCountsService.getCartItems(httpSession).get(id));
        model.addAttribute("itemsToCartAction", itemsToCartAction);
        model.addAttribute("totalItemsCounts", sessionItemsCountsService.checkItemsCount(httpSession));
        model.addAttribute("buyAction", buyAction);
        return "item";
    }

    @PostMapping(itemsAction + "/{id}")
    public String increaseDecreaseItemCount(
            @PathVariable Long id,
            @RequestParam String action,
            HttpSession httpSession) {
        sessionItemsCountsService.updateItemCount(httpSession, id, action);
        return "redirect:" + itemsAction + "/" + id;
    }
}
