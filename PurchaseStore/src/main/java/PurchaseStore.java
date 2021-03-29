import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PurchaseStore {
    private static PurchaseStore purchaseStore;
    private Map<Integer,Map<String, Integer>> storePurchases;
    private Map<String,Map<Integer, Integer>> itemPurchases;

    public void addItemToStorePurchases(int storeId, String itemId, Integer quantityPurchased) {
        Map<String, Integer> storeItemsPurchased =
                storePurchases.getOrDefault(storeId, new ConcurrentHashMap<>());

        int currentTotal = storeItemsPurchased.getOrDefault(itemId, 0);
        storeItemsPurchased.put(itemId, quantityPurchased + currentTotal);
        storePurchases.put(storeId, storeItemsPurchased);
    }

    public void addStoreToItemPurchases(int storeId, String itemId, Integer quantityPurchased) {
        Map<Integer, Integer> storesForItem =
                itemPurchases.getOrDefault(itemId, new ConcurrentHashMap<>());

        int currentTotal = storesForItem.getOrDefault(itemId, 0);
        storesForItem.put(storeId, quantityPurchased + currentTotal);
        itemPurchases.put(itemId, storesForItem);
    }

    private PurchaseStore() {
        storePurchases = new ConcurrentHashMap<>();
        itemPurchases = new ConcurrentHashMap<>();
    }

    public static PurchaseStore getStoreInstance() {
        if (purchaseStore == null) {
            return new PurchaseStore();
        }
        return purchaseStore;
    }

    public List<String> getTopNItemsForStore(int n, int storeId) {
        ItemCountPair itemCount;
        PriorityQueue<ItemCountPair> itemCounts = new PriorityQueue<>();

        // if there have not been any purchases made at the given store
        if (!storePurchases.containsKey(storeId)) {
            return new ArrayList<String>();
        }

        // get items bought from the given store
        Map<String, Integer> itemsBoughtAtStore = storePurchases.get(storeId);

        // add items to the min heap of size at most n
        for (String itemId : itemsBoughtAtStore.keySet()) {
            itemCount = new ItemCountPair(itemId, itemsBoughtAtStore.get(itemId));
            itemCounts.add(itemCount);
            if (itemCounts.size() > n) {
                itemCounts.poll();
            }
        }
        List<String> results = new ArrayList<>();
        while (!itemCounts.isEmpty()) {
            results.add(itemCounts.poll().getFirst());
        }
        return results;
    }

    public Map<Integer, Map<String, Integer>> getStorePurchases() {
        return storePurchases;
    }

    public Map<String, Map<Integer, Integer>> getItemPurchases() {
        return itemPurchases;
    }
}
