package org.pl.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SessionItemsCountsService {

    public Map<Long, Integer> getCartItems(HttpSession session) {
        Map<Long, Integer> items = (Map<Long, Integer>) session.getAttribute("cartItems");
        if (items == null) {
            items = new HashMap<>();
            session.setAttribute("cartItems", items);
        }
        return items;
    }

    public void clearCartItems(HttpSession session) {
        Map<Long, Integer> items = (Map<Long, Integer>) session.getAttribute("cartItems");
        items.clear();
    }

    public void updateItemCount(HttpSession session, Long itemId, String action) {
        Map<Long, Integer> items = getCartItems(session);
        int current = items.getOrDefault(itemId, 0);

        switch (action) {
            case "PLUS" -> items.put(itemId, current + 1);
            case "MINUS" -> { if (current > 0) items.put(itemId, current - 1); }
            case "DELETE" -> items.remove(itemId);
        }
    }

    public int checkItemsCount(HttpSession httpSession) {
        getCartItems(httpSession).entrySet().removeIf(entry -> entry.getValue() == 0);
        return getCartItems(httpSession).values().stream().mapToInt(value -> value).sum();
    }
}

