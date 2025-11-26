package org.pl.utils;

import java.util.HashMap;
import java.util.Map;

public class ItemsUtils {

    public static Map<Long, Integer> itemsCounts = new HashMap<>();
    public static int totalItemsCounts = 0;

    public static void checkItemsCount() {
        itemsCounts.entrySet().removeIf(entry -> entry.getValue() == 0);
        totalItemsCounts = itemsCounts.values().stream().mapToInt(value -> value).sum();
    }
}
