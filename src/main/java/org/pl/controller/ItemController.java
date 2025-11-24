package org.pl.controller;

import org.pl.dao.Item;
import org.pl.dto.PagingInfoDto;
import org.pl.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.pl.controller.ItemController.Actions.itemsAction;


@Controller
@RequestMapping("/")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    static final class Actions {
        static final String itemsAction = "/items";
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
            Model model
    ) {
        // Учитываем, что пользователь видит страницы с 1, а Spring Data с 0
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<List<Item>> itemPage = itemService.getItemsSorted(pageable, sort, search);

        model.addAttribute("items", itemPage.getContent()); // Список списков!
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("paging", new PagingInfoDto(
                itemPage.getNumber() + 1,
                itemPage.getTotalPages(),
                itemPage.getSize(),
                itemPage.hasPrevious(),
                itemPage.hasNext()
        ));
        return "items";
    }
}
