import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
    private static final int NEW_ITEM_COUNT = 0;
    private Map<String,Integer> itemPurchaseCounts = new ConcurrentHashMap<>();
    private BoundedSortedList<ItemCountPair> topItemsPurchased = new BoundedSortedList<ItemCountPair>();


    public void addItemPurchase(String itemId, Integer quantityPurchased) {
        int currentTotalPurchased = itemPurchaseCounts.getOrDefault(itemId, NEW_ITEM_COUNT);
        int totalPurchased = currentTotalPurchased + quantityPurchased;
        itemPurchaseCounts.put(itemId, totalPurchased);

        ItemCountPair itemCountPair = new ItemCountPair(itemId, totalPurchased);
        topItemsPurchased.addItem(itemCountPair);
    }

    public List<ItemCountPair> getTopItemsPurchased() {
        return topItemsPurchased;
    }

}
