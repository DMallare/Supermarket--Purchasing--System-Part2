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

        int currentTotal = storesForItem.getOrDefault(storeId, 0);
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

    public StoreQueryResponse getTopNItemsForStore(int n, int storeId) {
        ItemCountPair itemCount;
        PriorityQueue<ItemCountPair> itemCounts = new PriorityQueue<>();
        StoreQueryResponse results = new StoreQueryResponse();
        List<ItemItemCount> resultItems = new ArrayList<>();
        results.setItems(resultItems);

        // if there have not been any purchases made at the given store
        if (!storePurchases.containsKey(storeId)) {
            return results;
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

        while (!itemCounts.isEmpty()) {
            itemCount = itemCounts.poll();
            int itemId = Integer.parseInt(itemCount.getFirst());
            int count = itemCount.getSecond();
            ItemItemCount item = new ItemItemCount(itemId, count);
            results.getItems().add(item);
        }
        return results;
    }

    public ItemQueryResponse getTopNStoresForItem(int n, Integer itemId) {
        StoreCountPair storeCount;
        PriorityQueue<StoreCountPair> storeCounts = new PriorityQueue<>();
        ItemQueryResponse results = new ItemQueryResponse();
        List<StoreItemCount> resultStores = new ArrayList<>();
        results.setStores(resultStores);

        // if there have not been any purchases made at the given store
        if (!itemPurchases.containsKey(itemId.toString())) {
            return results;
        }

        // get items bought from the given store
        Map<Integer, Integer> storesThatSoldItem = itemPurchases.get(itemId.toString());

        // add items to the min heap of size at most n
        for (Integer storeId : storesThatSoldItem.keySet()) {
            storeCount = new StoreCountPair(storeId, storesThatSoldItem.get(storeId));
            storeCounts.add(storeCount);
            if (storeCounts.size() > n) {
                storeCounts.poll();
            }
        }

        while (!storeCounts.isEmpty()) {
            storeCount = storeCounts.poll();
            StoreItemCount item = new StoreItemCount(storeCount.getFirst(), storeCount.getSecond());
            results.getStores().add(item);
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
